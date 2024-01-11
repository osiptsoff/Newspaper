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

    @Test
    public void findByLoginTest() {
        User foundUser = env.getUserService().findByLogin(env.getTestUser().getLogin());

        Assert.notNull(foundUser, "Must not be null");
        Assert.isTrue(foundUser.equals(env.getTestUser()), "Must be equal");
    }

    @Test
    public void tagAssociationTest() {
        String userLogin = env.getTestUser().getLogin();
        String tagName = env.getTestTag().getName();

        env.getUserService().likeTag(userLogin, tagName);
        Assert.isTrue(env.getUserService().isTagLiked(userLogin, tagName) == true, 
                "Tag must be liked");

        env.getUserService().dislikeTag(userLogin, tagName);
        Assert.isTrue(env.getUserService().isTagLiked(userLogin, tagName) == false, 
                "Tag must be disliked");

        env.getUserService().undoTagAssociation(userLogin, tagName);
        Assert.isTrue(env.getUserService().isTagLiked(userLogin, tagName) == null, 
                "User must not be associated with tag");
    }

    @Test
    public void newsLikeTest() {
        String userLogin = env.getTestUser().getLogin();
        Integer newsId = env.getTestNews().getId();

        env.getUserService().likeNews(userLogin, newsId);
        Assert.isTrue(env.getUserService().isNewsLiked(userLogin, newsId), 
                        "Must be liked");

        env.getUserService().undoLikeNews(userLogin, newsId);
        Assert.isTrue(env.getUserService().isNewsLiked(userLogin, newsId) == false, 
                        "Must not be liked");
    }
}
