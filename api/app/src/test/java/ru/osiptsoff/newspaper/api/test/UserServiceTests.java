package ru.osiptsoff.newspaper.api.test;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import ru.osiptsoff.newspaper.api.environment.UserServiceTestEnvironment;
import ru.osiptsoff.newspaper.api.model.User;

@SpringBootTest
public class UserServiceTests {
    private final UserServiceTestEnvironment env;

    @Autowired
    public UserServiceTests(UserServiceTestEnvironment env) {
        this.env = env;
    }

    @Test
    public void saveAndDeleteTest() {
        User testUser = new User();
        testUser.setLogin("Save user test user");
        testUser.setPassword("12345");

        testUser = env.getUserService().saveUser(testUser);

        Optional<User> result = env.getUserRepository().findById(testUser.getId());

        Assert.isTrue(result.isPresent(), "Saved user must be present");
        Assert.isTrue(result.get().getLogin().equals(testUser.getLogin()), "Logins must be equal");

        env.getUserService().deleteUser(testUser);
        result = env.getUserRepository().findById(testUser.getId());

        Assert.isTrue(!result.isPresent(), "Deleted user must not be present");
    }
}
