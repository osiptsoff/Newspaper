package ru.osiptsoff.newspaper.api.environment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.osiptsoff.newspaper.api.model.News;
import ru.osiptsoff.newspaper.api.model.NewsContentBlock;
import ru.osiptsoff.newspaper.api.model.NewsContentBlockId;
import ru.osiptsoff.newspaper.api.repository.NewsRepository;
import ru.osiptsoff.newspaper.api.repository.TagRepository;
import ru.osiptsoff.newspaper.api.service.NewsService;

@Component
@RequiredArgsConstructor
@Getter
public class NewsServiceTestEnvironment {
    private final NewsService newsService;
    private final TagRepository tagRepository;
    private final NewsRepository newsRepository;

    @Setter
    @Value("${app.config.textBlockPageSize}")
    private Integer blocksPerPage;

    private News smallNews;
    private News bigNews;

    @PostConstruct
    public void createTestNews() {
        smallNews = new News();
        smallNews.setTitle("Small test news");
        smallNews.setContent( Arrays.asList(
                new NewsContentBlock(new NewsContentBlockId(null, 1), smallNews, "First"),
                new NewsContentBlock(new NewsContentBlockId(null, 2), smallNews, "Second")
        ) );

        smallNews = newsRepository.save(smallNews);

        bigNews = new News();
        bigNews.setTitle("Big test news");
        List<NewsContentBlock> bigNewsContent = new ArrayList<NewsContentBlock>();
        for(int i = 0; i < 3 * blocksPerPage; i++)
            bigNewsContent.add(new NewsContentBlock(new NewsContentBlockId(null, i), bigNews, "Block " + i));
        bigNews.setContent(bigNewsContent);

        bigNews = newsRepository.save(bigNews);
    }

    @PreDestroy
    public void deleteTestNews() {
        newsRepository.delete(smallNews);
        newsRepository.delete(bigNews);
    }
}
