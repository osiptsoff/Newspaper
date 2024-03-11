package ru.spb.nicetu.newspaper.api.service.util;

import java.util.Optional;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ru.spb.nicetu.newspaper.api.model.User;
import ru.spb.nicetu.newspaper.api.model.auth.UserPrincipal;
import ru.spb.nicetu.newspaper.api.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class SecurityUserUtil {
    private final UserRepository userRepository;

    public UserDetails userToDetails(User user) {
        return new UserPrincipal(user);
    }

    public User getAuthenticatedUser() {
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) {
            throw new BadCredentialsException("Unauthorized");
        }
            
        String login = authentication.getPrincipal().toString();

        Optional<User> res = userRepository.findByLogin(login);
        if(!res.isPresent()) {
            throw new BadCredentialsException("Unauthorized");
        }

        return res.get();
    }
}
