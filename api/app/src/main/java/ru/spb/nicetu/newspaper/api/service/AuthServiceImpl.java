package ru.spb.nicetu.newspaper.api.service;

import java.util.HashSet;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.spb.nicetu.newspaper.api.model.User;
import ru.spb.nicetu.newspaper.api.model.auth.Role;
import ru.spb.nicetu.newspaper.api.model.auth.Token;
import ru.spb.nicetu.newspaper.api.repository.RoleRepository;
import ru.spb.nicetu.newspaper.api.repository.TokenRepository;
import ru.spb.nicetu.newspaper.api.repository.UserRepository;
import ru.spb.nicetu.newspaper.api.security.jwt.JwtUtility;
import ru.spb.nicetu.newspaper.api.service.exception.UnregistredTokenException;
import ru.spb.nicetu.newspaper.api.service.exception.UsernameTakenException;

/**
 * <p>{@link AuthService} implementation.</p>
 * 
 * <p>Logs its work and unpredicted exceptions.</p>
    * @author Nikita Osiptsov
    * @see {@link UserRepository}
    * @see {@link TokenRepository}
    * @see {@link RoleRepository}
    * @see {@link UserServiceImpl}
    * @see {@link JwtUtility}
 * @since 1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final RoleRepository roleRepository;

    private final JwtUtility jwtUtility;
    private final SecurityUserService securityUserService;
    private final PasswordEncoder passwordEncoder;

    @Setter
    @Value("${app.config.security.defaultUserRole}")
    private String defaultUserRoleName;
    @Setter
    @Value("${app.config.security.persistToken}")
    private Boolean tokensPersistent;

    private Role defaultUserRole;

    @PostConstruct
    public void setDefaultUserRole() {
        defaultUserRole = roleRepository.findByName(defaultUserRoleName).get();
    }

    @Transactional
    @Override
    public User register(String login, String password, String name, String lastName) {
        log.info("Got request for registration");

        if(userRepository.existsByLogin(login)) {
            throw new UsernameTakenException("Username already taken");
        }

        User user = new User();
        user.setLogin(login);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
        user.setLastName(lastName);
        user.setRoles(new HashSet<Role>());
        user.getRoles().add(defaultUserRole);

        return userRepository.save(user);
    }

    @Transactional
    @Override
    public String authenticate(String login, String password) {
        log.info("Got request for authentication");

        try {
            Optional<User> userOptional = userRepository.findByLogin(login);

            if(!userOptional.isPresent()) {
                log.info("Attempt to authenticate incorrect username");

                throw new BadCredentialsException("Invalid username or password");
            }

            User user = userOptional.get();

            if(!passwordEncoder.matches(password, user.getPassword())) {
                log.info("Attempt to authenticate incorrect password");

                throw new BadCredentialsException("Invalid username or password");
            }

            String token = jwtUtility.generateRefreshToken(securityUserService.userToDetails(user));
            
            if(tokensPersistent) {
                Token dbToken = new Token(user.getId(), token, user);
                user.setToken(dbToken);
                userRepository.save(user);
            }

            log.info("Completed authentication");

            return token;
        } catch (BadCredentialsException e) {
            throw e;
        } catch(Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }

    @Override
    public void logout(String refreshToken) {
        log.info("Got request to log out");

        try {
            if(!tokensPersistent) {
                log.info("Application does not persist tokens, no way to log out");
                return;
            }

            if(tokenRepository.deleteByValue(refreshToken) > 0) {
                SecurityContextHolder.getContext().setAuthentication(null);
                log.info("Logged out");
            } else {
                log.info("Request to log out was unauthorized");
            }
        } catch(Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }

    @Override
    public String refresh(String refreshToken) {
        log.info("Got request to get access token");

        try {
            if(tokensPersistent && !tokenRepository.existsByValue(refreshToken)) {
                log.info("Got unregistered refresh token");

                throw new UnregistredTokenException("Token is not registered");
            }

            UserDetails userDetails = jwtUtility.parseAndValidateRefreshToken(refreshToken);
            if(userDetails == null) {
                log.info("Refresh token is invalid or expired");

                throw new JwtException("Token is invalid or expired");
            }

            String accessToken = jwtUtility.generateAccessToken(userDetails);

            log.info("Refreshed token");

            return accessToken;
        } catch (BadCredentialsException | JwtException | UnregistredTokenException e) {
            throw e;
        } catch(Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }

    @Override
    public Integer getRefreshLifespawn() {
        return jwtUtility.getRefreshLifespawn();
    }
}
