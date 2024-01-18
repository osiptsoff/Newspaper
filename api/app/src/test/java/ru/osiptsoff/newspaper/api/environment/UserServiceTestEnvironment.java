package ru.osiptsoff.newspaper.api.environment;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.osiptsoff.newspaper.api.model.News;
import ru.osiptsoff.newspaper.api.model.Tag;
import ru.osiptsoff.newspaper.api.model.User;
import ru.osiptsoff.newspaper.api.repository.NewsRepository;
import ru.osiptsoff.newspaper.api.repository.TagRepository;
import ru.osiptsoff.newspaper.api.repository.UserRepository;
import ru.osiptsoff.newspaper.api.service.UserService;

@Component
@RequiredArgsConstructor
@Getter
public class UserServiceTestEnvironment {
    private final UserService userService;

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
