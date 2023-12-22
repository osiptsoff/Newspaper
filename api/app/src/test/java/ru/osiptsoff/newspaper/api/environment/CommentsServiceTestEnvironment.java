package ru.osiptsoff.newspaper.api.environment;

import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.osiptsoff.newspaper.api.model.News;
import ru.osiptsoff.newspaper.api.model.NewsContentBlock;
import ru.osiptsoff.newspaper.api.model.NewsContentBlockId;
import ru.osiptsoff.newspaper.api.repository.CommentRepository;
import ru.osiptsoff.newspaper.api.repository.NewsRepository;
import ru.osiptsoff.newspaper.api.service.CommentsService;

@Component
@RequiredArgsConstructor
public class CommentsServiceTestEnvironment {
    private final NewsRepository newsRepository;

    private @Getter final CommentRepository commentRepository;
    private @Getter final CommentsService commentService;

    private @Getter News testNews;

    @PostConstruct
    public void createTestNews() {
        testNews = new News();
        testNews.setTitle("News object to test comments");
        testNews.setContent( Arrays.asList(
                new NewsContentBlock(new NewsContentBlockId(null, 1), testNews, "First"),
                new NewsContentBlock(new NewsContentBlockId(null, 2), testNews, "Second")
        ) );

        testNews = newsRepository.save(testNews);
    }

    @PreDestroy
    public void deleteTestNews() {
        newsRepository.delete(testNews);
    }
}
