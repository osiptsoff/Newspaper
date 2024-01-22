package ru.spb.nicetu.newspaper.api.test;

import java.util.HashSet;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import ru.spb.nicetu.newspaper.api.environment.UserServiceTestEnvironment;
import ru.spb.nicetu.newspaper.api.model.Tag;
import ru.spb.nicetu.newspaper.api.model.User;
import ru.spb.nicetu.newspaper.api.model.UserTag;
import ru.spb.nicetu.newspaper.api.model.embeddable.UserTagId;

@SpringBootTest
@Transactional
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
        Long newsId = env.getTestNews().getId();

        env.getUserService().likeNews(userLogin, newsId);
        Assert.isTrue(env.getUserService().isNewsLiked(userLogin, newsId), 
            "Must be liked");

        env.getUserService().undoLikeNews(userLogin, newsId);
        Assert.isTrue(env.getUserService().isNewsLiked(userLogin, newsId) == false, 
            "Must not be liked");
    }

    @Test
    public void fetchWithTagsTest() {
        User user = new User();
        user.setLogin("Fetch tags user");
        user.setPassword("12345");
        user.setTags(new HashSet<UserTag>());

        Tag likedTag = new Tag();
        likedTag.setName("He likes me");
        likedTag = env.getTagRepository().save(likedTag);
        Tag dislikedTag = new Tag();
        dislikedTag.setName("He doesn't like me");
        dislikedTag = env.getTagRepository().save(dislikedTag);

        UserTag likingRelation = new UserTag();
        likingRelation.setUserTagId(new UserTagId());
        likingRelation.setLikes(true);
        likingRelation.setUser(user);
        likingRelation.setTag(likedTag);
        UserTag dislikingRelation = new UserTag();
        dislikingRelation.setUserTagId(new UserTagId());
        dislikingRelation.setLikes(false);
        dislikingRelation.setUser(user);
        dislikingRelation.setTag(dislikedTag);

        user.getTags().add(likingRelation);
        user.getTags().add(dislikingRelation);

        user = env.getUserRepository().save(user);

        User fetchedUser = env.getUserService().findByLoginFetchTags(user.getLogin());

        Assert.isTrue(fetchedUser.getLikedTags().size() == 1
            && fetchedUser.getLikedTags().contains(likedTag),
            "Must like tag");
        Assert.isTrue(fetchedUser.getDislikedTags().size() == 1
            && fetchedUser.getDislikedTags().contains(dislikedTag),
            "Must like tag");

        env.getTagRepository().delete(likedTag);
        env.getTagRepository().delete(dislikedTag);
        env.getUserRepository().delete(user);
    }
}
