package ru.spb.nicetu.newspaper.api.environment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.spb.nicetu.newspaper.api.model.News;
import ru.spb.nicetu.newspaper.api.model.NewsContentBlock;
import ru.spb.nicetu.newspaper.api.model.embeddable.NewsContentBlockId;
import ru.spb.nicetu.newspaper.api.repository.NewsRepository;
import ru.spb.nicetu.newspaper.api.repository.TagRepository;
import ru.spb.nicetu.newspaper.api.service.NewsServiceImpl;
import ru.spb.nicetu.newspaper.api.test.NewsServiceTests;

/**
 * <p>Environment used in {@code NewsServiceTests}.</p>
 *
 * <p>Contains tested service, repositories to load related resources, testing entities.</p>
    * @author Nikita Osiptsov
    * @see {@link NewsServiceTests}
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
@Getter
public class NewsServiceTestEnvironment {
    private final NewsServiceImpl newsService;
    private final TagRepository tagRepository;
    private final NewsRepository newsRepository;

    @Setter
    @Value("${app.config.textBlockPageSize}")
    private Integer blocksPerPage;

    private News smallNews;
    private News bigNews;

    @PostConstruct
    @Transactional
    public void createTestNews() {
        smallNews = new News();
        smallNews.setTitle("Small test news");
        smallNews.setContentBlocks(Arrays.asList(
            new NewsContentBlock(new NewsContentBlockId(null, 1L), smallNews, "First"),
            new NewsContentBlock(new NewsContentBlockId(null, 2L), smallNews, "Second")));
        smallNews = newsRepository.save(smallNews);

        bigNews = new News();
        bigNews.setTitle("Big test news");
        List<NewsContentBlock> bigNewsContent = new ArrayList<NewsContentBlock>();
        for(long i = 0; i < 3 * blocksPerPage; i++) {
            bigNewsContent.add(new NewsContentBlock(new NewsContentBlockId(null, i), bigNews, "Block " + i));
        }
    
        bigNews.setContentBlocks(bigNewsContent);

        bigNews = newsRepository.save(bigNews);
    }

    @PreDestroy
    @Transactional
    public void deleteTestNews() {
        newsRepository.delete(smallNews);
        newsRepository.delete(bigNews);
    }
}
