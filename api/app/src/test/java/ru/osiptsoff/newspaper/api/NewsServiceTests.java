package ru.osiptsoff.newspaper.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import ru.osiptsoff.newspaper.api.model.News;
import ru.osiptsoff.newspaper.api.model.NewsContentBlock;
import ru.osiptsoff.newspaper.api.model.NewsContentBlockId;
import ru.osiptsoff.newspaper.api.model.Tag;
import ru.osiptsoff.newspaper.api.repository.NewsRepository;
import ru.osiptsoff.newspaper.api.repository.TagRepository;
import ru.osiptsoff.newspaper.api.service.NewsService;
import ru.osiptsoff.newspaper.api.service.auxiliary.NewsServiceFindNewsByIdResult;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class NewsServiceTests {
    private final NewsService newsService;
    private final TagRepository tagRepository;
    private final NewsRepository newsRepository;

    private static News firstNews;
    private static News secondNews;
    private static News associationNews;

    private static Tag testTag;

    @Autowired
    public NewsServiceTests(NewsService newsService, TagRepository tagRepository, NewsRepository newsRepository) {
        this.newsService = newsService;
        this.tagRepository = tagRepository;
        this.newsRepository = newsRepository;
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
    public void updateTest() {
        System.out.println(firstNews);

        firstNews.setTitle("First test news updated");
        firstNews.setContent( Arrays.asList(
                new NewsContentBlock(new NewsContentBlockId(firstNews.getId(), 1), firstNews, "First updated"),
                new NewsContentBlock(new NewsContentBlockId(firstNews.getId(), 2), firstNews, "Second updated"),
                new NewsContentBlock(new NewsContentBlockId(firstNews.getId(), 3), firstNews, "Third")
        ) );
        firstNews.setPostTime(OffsetDateTime.now());

        firstNews = newsService.saveNews(firstNews);
    }

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
        NewsServiceFindNewsByIdResult res = newsService.findNewsById(firstNews.getId());

        System.out.println(res);

        Assert.notNull(res, "Must not get null");
        Assert.isTrue(res.getIsLastCommentsPage(), "Must be true");
        Assert.isTrue(res.getIsLastContentPage(), "Must be true");
    }

    @Test
    public void getOnePartiallyFetchTest() {
        NewsServiceFindNewsByIdResult res = newsService.findNewsById(secondNews.getId());

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

    @Test
    public void tagAssociationTest() {
        testTag = new Tag();
        testTag.setName("Test tag");

        tagRepository.save(testTag);
        associationNews = new News();
        associationNews.setTitle("Second test news");
        associationNews = newsRepository.save(associationNews);
        
        newsService.associateWithTag(associationNews.getId(), testTag.getName());
        associationNews = newsRepository.findById(associationNews.getId()).get();

        Assert.isTrue(associationNews.getTags().size() == 1, "Must have one tag, but have " + associationNews.getTags().size());


        newsService.deassociateWithTag(associationNews.getId(), testTag.getName());

        associationNews = newsRepository.findById(associationNews.getId()).get();

        Assert.isTrue(associationNews.getTags().size() == 0, "Must have no tags, but have " + associationNews.getTags().size());

        newsRepository.delete(associationNews);
        tagRepository.delete(testTag);
    }
}
