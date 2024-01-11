package ru.osiptsoff.newspaper.api.service;

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
import ru.osiptsoff.newspaper.api.model.User;
import ru.osiptsoff.newspaper.api.model.auth.Role;
import ru.osiptsoff.newspaper.api.model.auth.Token;
import ru.osiptsoff.newspaper.api.repository.RoleRepository;
import ru.osiptsoff.newspaper.api.repository.TokenRepository;
import ru.osiptsoff.newspaper.api.repository.UserRepository;
import ru.osiptsoff.newspaper.api.security.jwt.JwtUtility;
import ru.osiptsoff.newspaper.api.service.exceptions.MissingEntityException;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final RoleRepository roleRepository;

    private final UserService userService;

    private final JwtUtility jwtUtility;
    private final PasswordEncoder passwordEncoder;

    @Setter
    @Value("${app.config.security.defaultUserRole}")
    private String defaultUserRoleName;
    private Role defaultUserRole;

    @PostConstruct
    public void setDefaultUserRole() {
        defaultUserRole = roleRepository.findByName(defaultUserRoleName).get();
    }

    public User register(String login, String password) {
        log.info("Got request for registration");

        User user = new User();
        user.setLogin(login);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(new HashSet<Role>());
        user.getRoles().add(defaultUserRole);

        return userRepository.save(user);
    }

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

            String token = jwtUtility.generateRefreshToken(userService.userToDetails(user));
            
            Token dbToken = new Token(user.getId(), token, user);

            user.setToken(dbToken);
            userRepository.save(user);

            log.info("Completed authentication");

            return token;

        } catch(Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }

    public void logout(String refreshToken) {
        log.info("Got request to log out");

        try {
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

    public String refresh(String refreshToken) {
        log.info("Got request to get access token");

        try {
            Optional<Token> dbTokenOptional = tokenRepository.findByValue(refreshToken);
            if(!dbTokenOptional.isPresent()) {
                log.info("Got unregistered refresh token");

                throw new MissingEntityException("Token is not registered");
            }
            Token dbToken = dbTokenOptional.get();

            UserDetails userDetails = jwtUtility.parseAndValidateRefreshToken(dbToken.getValue());
            if(userDetails == null) {
                log.info("Refresh token expired");

                tokenRepository.delete(dbToken);

                throw new JwtException("Token expired");
            }

            String accessToken = jwtUtility.generateAccessToken(userDetails);

            log.info("Refreshed token");

            return accessToken;
        } catch(Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }
}
