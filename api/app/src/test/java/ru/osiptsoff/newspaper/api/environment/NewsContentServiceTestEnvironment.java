package ru.osiptsoff.newspaper.api.environment;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.osiptsoff.newspaper.api.model.News;
import ru.osiptsoff.newspaper.api.repository.NewsContentRepository;
import ru.osiptsoff.newspaper.api.repository.NewsRepository;
import ru.osiptsoff.newspaper.api.service.NewsContentService;

@Component
@RequiredArgsConstructor
@Getter
public class NewsContentServiceTestEnvironment {
    private final NewsRepository newsRepository;

    private final NewsContentRepository newsContentRepository;
    private final NewsContentService newsContentService;

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
