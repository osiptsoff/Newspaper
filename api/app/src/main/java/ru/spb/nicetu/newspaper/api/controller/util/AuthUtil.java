package ru.spb.nicetu.newspaper.api.controller.util;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {
    public void checkIfAuthenticated(String login) throws BadCredentialsException {
        String authenticatedUserLogin = SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getPrincipal()
            .toString();

        if(!login.equals(authenticatedUserLogin)) {
            throw new BadCredentialsException("Given user is not authenticated");
        }
    }

    public String getAuthenticatedUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }
}
