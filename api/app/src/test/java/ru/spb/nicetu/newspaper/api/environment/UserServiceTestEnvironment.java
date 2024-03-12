package ru.spb.nicetu.newspaper.api.environment;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.spb.nicetu.newspaper.api.model.News;
import ru.spb.nicetu.newspaper.api.model.Tag;
import ru.spb.nicetu.newspaper.api.model.User;
import ru.spb.nicetu.newspaper.api.repository.NewsRepository;
import ru.spb.nicetu.newspaper.api.repository.TagRepository;
import ru.spb.nicetu.newspaper.api.repository.UserRepository;
import ru.spb.nicetu.newspaper.api.service.UserServiceImpl;

/**
 * <p>Environment used in {@code UserServiceTestEnvironment}.</p>
 *
 * <p>Contains tested service, repositories to load related resources, testing entities.</p>
    * @author Nikita Osiptsov
    * @see {@link UserServiceTestEnvironment}
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
@Getter
public class UserServiceTestEnvironment {
    private final UserServiceImpl userService;

    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final NewsRepository newsRepository;

    private User testUser;
    private Tag testTag;
    private News testNews;

    @PostConstruct
    @Transactional
    public void createTestEntities() {
        testUser = new User();
        testUser.setLogin("Test login");
        testUser.setPassword("test password");
        testUser = userRepository.save(testUser);

        testTag = new Tag();
        testTag.setName("User service test tag");
        testTag = tagRepository.save(testTag);

        testNews = new News();
        testNews.setTitle("User service test news");
        testNews = newsRepository.save(testNews);
    }

    @PreDestroy
    @Transactional
    public void deleteTestEntities() {
        userRepository.delete(testUser);
        tagRepository.delete(testTag);
        newsRepository.delete(testNews);
    }
}
