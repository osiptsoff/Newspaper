package ru.osiptsoff.newspaper.api.environment;

import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.osiptsoff.newspaper.api.model.News;
import ru.osiptsoff.newspaper.api.model.NewsContentBlock;
import ru.osiptsoff.newspaper.api.model.User;
import ru.osiptsoff.newspaper.api.model.embeddable.NewsContentBlockId;
import ru.osiptsoff.newspaper.api.repository.CommentRepository;
import ru.osiptsoff.newspaper.api.repository.NewsRepository;
import ru.osiptsoff.newspaper.api.repository.UserRepository;
import ru.osiptsoff.newspaper.api.service.CommentsService;

@Component
@RequiredArgsConstructor
public class CommentsServiceTestEnvironment {
    private final NewsRepository newsRepository;

    private @Getter final CommentRepository commentRepository;
    private @Getter final UserRepository userRepository;
    private @Getter final CommentsService commentService;

    private @Getter News testNews;
    private @Getter User testUser;

    @PostConstruct
    @Transactional
    public void createTestEntities() {
        testNews = new News();
        testNews.setTitle("News object to test comments");
        testNews.setContent( Arrays.asList(
                new NewsContentBlock(new NewsContentBlockId(null, 1), testNews, "First"),
                new NewsContentBlock(new NewsContentBlockId(null, 2), testNews, "Second")
        ) );
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
