package ru.spb.nicetu.newspaper.api.environment;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.spb.nicetu.newspaper.api.model.News;
import ru.spb.nicetu.newspaper.api.repository.NewsContentRepository;
import ru.spb.nicetu.newspaper.api.repository.NewsRepository;
import ru.spb.nicetu.newspaper.api.service.NewsContentServiceImpl;
import ru.spb.nicetu.newspaper.api.test.NewsContentServiceTests;

/**
 * <p>Environment used in {@code NewsContentServiceTests}.</p>
 *
 * <p>Contains tested service, repositories to load related resources, testing entities.</p>
    * @author Nikita Osiptsov
    * @see {@link NewsContentServiceTests}
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
@Getter
public class NewsContentServiceTestEnvironment {
    private final NewsRepository newsRepository;

    private final NewsContentRepository newsContentRepository;
    private final NewsContentServiceImpl newsContentService;

    private News testNews;

    @PostConstruct
    @Transactional
    public void createTestNews() {
        testNews = new News();
        testNews.setTitle("News object to test content");

        testNews = newsRepository.save(testNews);
    }

    @PreDestroy
    @Transactional
    public void deleteTestNews() {
        newsRepository.delete(testNews);
    }
}
