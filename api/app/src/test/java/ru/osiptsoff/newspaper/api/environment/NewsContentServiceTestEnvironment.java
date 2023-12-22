package ru.osiptsoff.newspaper.api.environment;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.osiptsoff.newspaper.api.model.News;
import ru.osiptsoff.newspaper.api.repository.NewsContentRepository;
import ru.osiptsoff.newspaper.api.repository.NewsRepository;
import ru.osiptsoff.newspaper.api.service.NewsContentService;

@Component
@RequiredArgsConstructor
public class NewsContentServiceTestEnvironment {
    @Getter
    private final NewsRepository newsRepository;

    private @Getter final NewsContentRepository newsContentRepository;
    private @Getter final NewsContentService newsContentService;

    private @Getter News testNews;

    @PostConstruct
    public void createTestNews() {
        testNews = new News();
        testNews.setTitle("News object to test content");

        testNews = newsRepository.save(testNews);
    }

    @PreDestroy
    public void deleteTestNews() {
        newsRepository.delete(testNews);
    }
}
