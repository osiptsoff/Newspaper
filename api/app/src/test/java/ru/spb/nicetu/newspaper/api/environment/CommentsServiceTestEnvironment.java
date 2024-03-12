package ru.spb.nicetu.newspaper.api.environment;

import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.spb.nicetu.newspaper.api.model.News;
import ru.spb.nicetu.newspaper.api.model.NewsContentBlock;
import ru.spb.nicetu.newspaper.api.model.User;
import ru.spb.nicetu.newspaper.api.model.embeddable.NewsContentBlockId;
import ru.spb.nicetu.newspaper.api.repository.CommentRepository;
import ru.spb.nicetu.newspaper.api.repository.NewsRepository;
import ru.spb.nicetu.newspaper.api.repository.UserRepository;
import ru.spb.nicetu.newspaper.api.service.CommentsServiceImpl;
import ru.spb.nicetu.newspaper.api.test.CommentsServiceTests;

/**
 * <p>Environment used in {@code CommentsServiceTests}.</p>
 *
 * <p>Contains tested service, repositories to load related resources, testing entities.</p>
    * @author Nikita Osiptsov
    * @see {@link CommentsServiceTests}
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
public class CommentsServiceTestEnvironment {
    private final NewsRepository newsRepository;

    private @Getter final CommentRepository commentRepository;
    private @Getter final UserRepository userRepository;
    private @Getter final CommentsServiceImpl commentService;

    private @Getter News testNews;
    private @Getter User testUser;

    @PostConstruct
    @Transactional
    public void createTestEntities() {
        testNews = new News();
        testNews.setTitle("News object to test comments");
        testNews.setContentBlocks(Arrays.asList(
            new NewsContentBlock(new NewsContentBlockId(null, 1L), testNews, "First"),
            new NewsContentBlock(new NewsContentBlockId(null, 2L), testNews, "Second")));
        testNews = newsRepository.save(testNews);

        testUser = new User();
        testUser.setLogin("Test user for comments tests");
        testUser.setPassword("12345");
        testUser = userRepository.save(testUser);
    }

    @PreDestroy
    @Transactional
    public void deleteTestEntities() {
        newsRepository.delete(testNews);
        userRepository.delete(testUser);
    }
}
