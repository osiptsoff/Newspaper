package ru.spb.nicetu.newspaper.api.service;

import org.springframework.security.core.userdetails.UserDetails;

import ru.spb.nicetu.newspaper.api.model.User;

/**
 * <p>Service which encapsulates Spring security relative business logic for users.</p>
 *
 * <p>Can be used to get currently authenticated user,
 *  convert {@link User} instance to {@link UserDetails} instance.</p>
    * @author Nikita Osiptsov
    * @see {@link User}
    * @see {@link UserDetails}
 * @since 1.2
 */
public interface SecurityUserService {
    UserDetails userToDetails(User user);
    User getAuthenticatedUser();
}
