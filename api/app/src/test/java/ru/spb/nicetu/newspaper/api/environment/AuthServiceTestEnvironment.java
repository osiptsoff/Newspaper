package ru.spb.nicetu.newspaper.api.environment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.spb.nicetu.newspaper.api.repository.TokenRepository;
import ru.spb.nicetu.newspaper.api.repository.UserRepository;
import ru.spb.nicetu.newspaper.api.service.AuthServiceImpl;
import ru.spb.nicetu.newspaper.api.test.AuthServiceTests;

/**
 * <p>Environment used in {@code AuthServiceTests}.</p>
 *
 * <p>Contains tested service, repositories to load related resources, testing entities.</p>
    * @author Nikita Osiptsov
    * @see {@link AuthServiceTests}
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
@Getter
public class AuthServiceTestEnvironment {
    private final AuthServiceImpl authService;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    private final String userLogin = "Auth test user";
    private final String userRawPassword = "12345";
    private final String userName = "Vadim";
    private final String userLastName = "Akimov";

    @Setter
    @Value("${app.config.security.persistToken}")
    private Boolean tokensPersistent;
}
