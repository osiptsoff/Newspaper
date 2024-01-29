package ru.spb.nicetu.newspaper.api.controller.util;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * <p>Utility class for use by controllers which work depends on user authenticated.</p>
 *
 * <p>This class provides methods for getting username of currently authenticated user
 * and checking if given login is authorized in current context.</p>
    * @author Nikita Osiptsov
 * @since 1.0
 */
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
