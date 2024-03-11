package ru.spb.nicetu.newspaper.api.service;

import ru.spb.nicetu.newspaper.api.model.User;

/**
 * <p>Service which encapsulates business logic for registration and authentication.</p>
 * 
 * <p>Can be used to register users, authenticate users, refreshing access token and logout.</p>
    * @author Nikita Osiptsov
 * @since 1.1
 */
public interface AuthService {
    User register(String login, String password, String name, String lastName);
    String authenticate(String login, String password);
    void logout(String refreshToken);
    String refresh(String refreshToken);

}
