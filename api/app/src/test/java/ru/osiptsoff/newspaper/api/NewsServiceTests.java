package ru.osiptsoff.newspaper.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import ru.osiptsoff.newspaper.api.environment.NewsServiceTestEnvironment;
import ru.osiptsoff.newspaper.api.model.News;
import ru.osiptsoff.newspaper.api.model.NewsContentBlock;
import ru.osiptsoff.newspaper.api.model.Tag;
import ru.osiptsoff.newspaper.api.model.embeddable.NewsContentBlockId;
import ru.osiptsoff.newspaper.api.service.auxiliary.NewsServiceFindNewsByIdResult;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SpringBootTest
public class NewsServiceTests {
    private final NewsServiceTestEnvironment env;

    @Autowired
    public NewsServiceTests(NewsServiceTestEnvironment env) {
        this.env = env;
    }

    @Test
    public void saveAndDeleteTest() {
        News testNews = new News();
        testNews.setTitle("Test news save");
        testNews.setContent( Arrays.asList(
                new NewsContentBlock(new NewsContentBlockId(null, 1), testNews, "First"),
                new NewsContentBlock(new NewsContentBlockId(null, 2), testNews, "Second")
        ) );

        testNews = env.getNewsService().saveNews(testNews);

        Optional<News> result = env.getNewsRepository().findById(testNews.getId());

        Assert.isTrue(result.isPresent(), "Saved news must be present");
        Assert.isTrue(result.get().getTitle().equals(testNews.getTitle()), "Headers must be equal");

        env.getNewsService().deleteNews(testNews);
        result = env.getNewsRepository().findById(testNews.getId());

        Assert.isTrue(!result.isPresent(), "Deleted news must not be present");
    };


    @Test
    public void getAllTest() {
        List<News> newsList = env.getNewsService().findAllNews();

        Assert.isTrue(newsList.size() >= 2, "At least two test news must be present");

        Assert.isTrue(newsList
                        .stream()
                        .map( n -> n.getTitle() )
                        .collect(Collectors.toList())
                        .contains(env.getSmallNews().getTitle()), "Small news must be present");
        Assert.isTrue(newsList
                        .stream()
                        .map( n -> n.getTitle() )
                        .collect(Collectors.toList())
                        .contains(env.getBigNews().getTitle()), "Big news must be present");
    }

    @Test
    public void getOneFullyFetchTest() {
        NewsServiceFindNewsByIdResult smallNews = env.getNewsService().findNewsById(env.getSmallNews().getId());

        Assert.notNull(smallNews, "Small news must be present");
        Assert.isTrue(smallNews.getNews().getContent().size() == env.getSmallNews().getContent().size(), "Sizes of content must be equal");
        Assert.isTrue(smallNews.getNews().getTitle().equals(env.getSmallNews().getTitle()), "Headers must be equal");
        Assert.isTrue(smallNews.getIsLastContentPage(), "This news must have only one content page");
    }

    @Test
    public void getOnePartiallyFetchTest() {
        NewsServiceFindNewsByIdResult bigNews = env.getNewsService().findNewsById(env.getBigNews().getId());

        Assert.notNull(bigNews, "Big news must be present");
        Assert.isTrue(bigNews.getNews().getContent().size() == env.getBlocksPerPage(), "Size must be equal to max size of page");
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
