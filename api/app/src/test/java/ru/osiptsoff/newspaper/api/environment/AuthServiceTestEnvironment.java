package ru.osiptsoff.newspaper.api.environment;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.osiptsoff.newspaper.api.repository.TokenRepository;
import ru.osiptsoff.newspaper.api.repository.UserRepository;
import ru.osiptsoff.newspaper.api.service.AuthService;

@Component
@RequiredArgsConstructor
@Getter
public class AuthServiceTestEnvironment {
    private final AuthService authService;
    private final TokenRepository tokenRepository;

    private final UserRepository userRepository;

    private final String userLogin = "Auth test user";
    private final String userRawPassword = "12345";
}
