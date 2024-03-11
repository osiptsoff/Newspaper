package ru.spb.nicetu.newspaper.api.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.util.Assert;

import ru.spb.nicetu.newspaper.api.environment.NewsServiceTestEnvironment;
import ru.spb.nicetu.newspaper.api.model.News;
import ru.spb.nicetu.newspaper.api.model.NewsContentBlock;
import ru.spb.nicetu.newspaper.api.model.Tag;
import ru.spb.nicetu.newspaper.api.model.embeddable.NewsContentBlockId;
import ru.spb.nicetu.newspaper.api.service.NewsService;
import ru.spb.nicetu.newspaper.api.service.auxiliary.NewsServiceFindNewsByIdResult;

import java.util.Arrays;
import java.util.Optional;

import javax.transaction.Transactional;

/**
 * <p>Tests for {@code NewsService} features.</p>
 *
 * <p>Contains tests for all methods of {@code NewsService}.</p>
    * @author Nikita Osiptsov
    * @see {@link NewsService}
 * @since 1.0
 */
@SpringBootTest
public class NewsServiceTests {
    private final NewsServiceTestEnvironment env;

    @Autowired
    public NewsServiceTests(NewsServiceTestEnvironment env) {
        this.env = env;
    }

    @Test
    @Transactional
    public void saveAndDeleteTest() {
        News testNews = new News();
        testNews.setTitle("Test news save");
        testNews.setContentBlocks(Arrays.asList(
            new NewsContentBlock(new NewsContentBlockId(null, 1L), testNews, "First"),
            new NewsContentBlock(new NewsContentBlockId(null, 2L), testNews, "Second")));

        testNews = env.getNewsService().saveNews(testNews);

        Optional<News> result = env.getNewsRepository().findById(testNews.getId());

        Assert.isTrue(result.isPresent(), "Saved news must be present");
        Assert.isTrue(result.get().getTitle().equals(testNews.getTitle()), "Headers must be equal");

        env.getNewsService().deleteNews(testNews);
        result = env.getNewsRepository().findById(testNews.getId());

        Assert.isTrue(!result.isPresent(), "Deleted news must not be present");
    };


    @Test
    @Transactional
    public void getAllTest() {
        Page<News> newsPage = env.getNewsService().findAllNews(1);

        Assert.isTrue(newsPage.getSize() >= 2, "At least two test news must be present");
    }

    @Test
    @Transactional
    public void getOneFullyFetchTest() {
        NewsServiceFindNewsByIdResult smallNews = env.getNewsService().findNewsById(env.getSmallNews().getId());

        Assert.notNull(smallNews, "Small news must be present");
        Assert.isTrue(smallNews.getNews().getContentBlocks().size() == env.getSmallNews().getContentBlocks().size(), "Sizes of content must be equal");
        Assert.isTrue(smallNews.getNews().getTitle().equals(env.getSmallNews().getTitle()), "Headers must be equal");
        Assert.isTrue(smallNews.getIsLastContentPage(), "This news must have only one content page");
    }

    @Test
    @Transactional
    public void getOnePartiallyFetchTest() {
        NewsServiceFindNewsByIdResult bigNews = env.getNewsService().findNewsById(env.getBigNews().getId());

        Assert.notNull(bigNews, "Big news must be present");
        Assert.isTrue(bigNews.getNews().getContentBlocks().size() == env.getBlocksPerPage(), "Size must be equal to max size of page");
        Assert.isTrue(bigNews.getNews().getTitle().equals(env.getBigNews().getTitle()), "Headers must be equal");
        Assert.isTrue(!bigNews.getIsLastContentPage(), "This news must have more content pages");
    }

    @Test
    public void tagAssociationTest() {
        Tag tag = new Tag();
        tag.setName("Tag association test tag");
        tag = env.getTagRepository().save(tag);

        env.getNewsService().associateWithTag(env.getSmallNews().getId(), tag.getName());
        Optional<News> smallNewsOptional = env.getNewsRepository().findById(env.getSmallNews().getId());
        Assert.isTrue(smallNewsOptional.get().getTags().contains(tag), "News must be associated with tag");

        env.getNewsService().deassociateWithTag(env.getSmallNews().getId(), tag.getName());
        smallNewsOptional = env.getNewsRepository().findById(env.getSmallNews().getId());
        Assert.isTrue(!smallNewsOptional.get().getTags().contains(tag), "News must not be associated with tag");

        env.getTagRepository().delete(tag);
    }
}