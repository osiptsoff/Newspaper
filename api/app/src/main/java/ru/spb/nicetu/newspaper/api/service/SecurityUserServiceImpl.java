package ru.spb.nicetu.newspaper.api.service;

import java.util.Optional;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.spb.nicetu.newspaper.api.model.User;
import ru.spb.nicetu.newspaper.api.model.auth.UserPrincipal;
import ru.spb.nicetu.newspaper.api.repository.UserRepository;

/**
 * <p>{@link SecurityUserService} implementation.</p>
 *
 * <p>Logs its work.</p>
    * @author Nikita Osiptsov
 * @since 1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SecurityUserServiceImpl implements SecurityUserService {
    private final UserRepository userRepository;

    @Override
    public UserDetails userToDetails(User user) {
        return new UserPrincipal(user);
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) {
            throw new BadCredentialsException("Unauthorized");
        }
            
        String login = authentication.getPrincipal().toString();

        Optional<User> res = userRepository.findByLogin(login);
        if(!res.isPresent()) {
            log.warn("Authenticated user is not present in db, login: '" + login + "'");
            throw new BadCredentialsException("Unauthorized");
        }

        return res.get();
    }
}
