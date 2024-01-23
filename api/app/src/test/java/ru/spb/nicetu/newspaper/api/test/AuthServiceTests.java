package ru.spb.nicetu.newspaper.api.test;

import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import ru.spb.nicetu.newspaper.api.environment.AuthServiceTestEnvironment;
import ru.spb.nicetu.newspaper.api.model.User;
import ru.spb.nicetu.newspaper.api.service.AuthService;

/**
 * <p>Tests for {@code AuthService} features.</p>
 *
 * <p>Contains tests for all methods of {@code AuthService}.</p>
    * @author Nikita Osiptsov
    * @see {@link AuthService}
 * @since 1.0
 */
@SpringBootTest
@Transactional
public class AuthServiceTests {
    private final AuthServiceTestEnvironment env;

    @Autowired
    public AuthServiceTests(AuthServiceTestEnvironment env) {
        this.env = env;
    }

    @Test
    public void authenticationComplexTest() {
        env.getAuthService().register(
            env.getUserLogin(),
            env.getUserRawPassword(),
            env.getUserName(),
            env.getUserLastName()
        );

        Optional<User> user = env.getUserRepository().findByLogin(env.getUserLogin());

        Assert.isTrue(user.isPresent(),"User must be persistent");
        Assert.isTrue(user.get().getRoles().size() > 0, "Must have role");


        String refreshToken = env.getAuthService().authenticate(env.getUserLogin(), env.getUserRawPassword());

        Assert.notNull(refreshToken, "Refresh token must not be null");
        Assert.isTrue(env.getTokenRepository().findByValue(refreshToken).isPresent(), 
            "Refresh token must be persistent");


        String accessToken = env.getAuthService().refresh(refreshToken);

        Assert.notNull(accessToken, "Access token must not be null");


        env.getAuthService().logout(refreshToken);

        Assert.isTrue(!env.getTokenRepository().findByValue(refreshToken).isPresent(),
            "Refresh token must not be persistent");

        env.getUserRepository().delete(user.get());
    }
}
