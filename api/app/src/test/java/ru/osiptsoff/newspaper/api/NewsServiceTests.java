package ru.osiptsoff.newspaper.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import ru.osiptsoff.newspaper.api.model.News;
import ru.osiptsoff.newspaper.api.model.NewsContentBlock;
import ru.osiptsoff.newspaper.api.model.NewsContentBlockId;
import ru.osiptsoff.newspaper.api.service.NewsService;
import ru.osiptsoff.newspaper.api.service.auxiliary.NewsServiceFindNewsByIdResponse;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class NewsServiceTests {
    private final NewsService newsService;

    private static News firstNews;
    private static News secondNews;

    @Autowired
    public NewsServiceTests(NewsService newsService) {
        this.newsService = newsService;
    }

    @Test
    public void loads() {
        Assert.notNull(newsService, "Service not loaded.");
    }

    @Test
    public void saveTest() {
        firstNews = new News();
        firstNews.setTitle("First test news");
        firstNews.setContent( Arrays.asList(
                new NewsContentBlock(new NewsContentBlockId(null, 1), firstNews, "First"),
                new NewsContentBlock(new NewsContentBlockId(null, 2), firstNews, "Second")
        ) );

        firstNews = newsService.saveNews(firstNews);
        System.out.println(firstNews);

        secondNews = new News();
        secondNews.setTitle("Second test news");
        secondNews.setContent( Arrays.asList(
                new NewsContentBlock(new NewsContentBlockId(null, 1), secondNews, "F"),
                new NewsContentBlock(new NewsContentBlockId(null, 2), secondNews, "S"),
                new NewsContentBlock(new NewsContentBlockId(null, 3), secondNews, "T"),
                new NewsContentBlock(new NewsContentBlockId(null, 4), secondNews, "F"),
                new NewsContentBlock(new NewsContentBlockId(null, 5), secondNews, "FF"),
                new NewsContentBlock(new NewsContentBlockId(null, 6), secondNews, "S"),
                new NewsContentBlock(new NewsContentBlockId(null, 7), secondNews, "SS")
        ) );

        secondNews = newsService.saveNews(secondNews);
        System.out.println(secondNews);
    };

    @Test
    public void getAllTest() {
        List<News> newsList = newsService.findAllNews();

        for(News news : newsList) {
            System.out.println(news.getTitle());
            System.out.println(news.getPostTime());
        }

        Assert.notEmpty(newsList, "Must not get empty list");
    }

    @Test
    public void getOneFullyFetchTest() {
        NewsServiceFindNewsByIdResponse res = newsService.findNewsById(firstNews.getId());

        System.out.println(res);

        Assert.notNull(res, "Must not get null");
        Assert.isTrue(res.getIsLastCommentsPage(), "Must be true");
        Assert.isTrue(res.getIsLastContentPage(), "Must be true");
    }

    @Test
    public void getOnePartiallyFetchTest() {
        NewsServiceFindNewsByIdResponse res = newsService.findNewsById(secondNews.getId());

        System.out.println(res);

        Assert.notNull(res, "Must not get null");
        Assert.isTrue(res.getIsLastCommentsPage(), "Must be true");
        Assert.isTrue(!res.getIsLastContentPage(), "Must be false");
    }

    @Test
    public void deleteTest() {
        newsService.deleteNews(firstNews);
        newsService.deleteNews(secondNews);
    }
}
