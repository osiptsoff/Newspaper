package ru.spb.nicetu.newspaper.api.test;

import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.util.Assert;


import ru.spb.nicetu.newspaper.api.environment.NewsContentServiceTestEnvironment;
import ru.spb.nicetu.newspaper.api.model.NewsContentBlock;
import ru.spb.nicetu.newspaper.api.model.embeddable.NewsContentBlockId;
import ru.spb.nicetu.newspaper.api.service.NewsContentService;

/**
 * <p>Tests for {@code NewsContentService} features.</p>
 *
 * <p>Contains tests for all methods of {@code NewsContentService}.</p>
    * @author Nikita Osiptsov
    * @see {@link NewsContentService}
 * @since 1.0
 */
@SpringBootTest
@Transactional
public class NewsContentServiceTests {
    private final NewsContentServiceTestEnvironment env;

    private final Integer blocksPerPage;

    @Autowired
    public NewsContentServiceTests(NewsContentServiceTestEnvironment env,
            @Value("${app.config.textBlockPageSize}") Integer blocksPerPage) {
        this.env = env;
        this.blocksPerPage = blocksPerPage;
    }

    @Test
    public void saveContentBlockTest() {
        NewsContentBlock block = new NewsContentBlock();
        block.setText("First block");
        block.setNewsContentBlockId(new NewsContentBlockId(null, 1L));
        block.setNews(env.getTestNews());

        block = env.getNewsContentService().saveNewsContentBlock(block);

        Optional<NewsContentBlock> dbBlock = env.getNewsContentRepository().findById(block.getNewsContentBlockId());

        Assert.isTrue(dbBlock.isPresent(), "Must be present");
        Assert.isTrue(dbBlock.get().getNewsContentBlockId().getNewsId() == env.getTestNews().getId(), "Must be tied to news");
    }
    
    @Test
    public void findPageTest() {
        for(long i = 0; i < blocksPerPage + 1; i++) {
            NewsContentBlock block = new NewsContentBlock();
            block.setText("Block" + i);
            block.setNewsContentBlockId(new NewsContentBlockId(null, i + 2));
            block.setNews(env.getTestNews());

            block = env.getNewsContentService().saveNewsContentBlock(block);
        }

        Page<NewsContentBlock> firstPage = env.getNewsContentService().findNthPageOfContent(env.getTestNews().getId(), 0);

        for(NewsContentBlock block : firstPage.getContent()) {
            System.out.println(block.getText());
        }
            
        Assert.isTrue(!firstPage.isLast(), "There must be more pages");

        Page<NewsContentBlock> secondPage = env.getNewsContentService().findNthPageOfContent(env.getTestNews().getId(), 1);

        for(NewsContentBlock block : secondPage.getContent()) {
            System.out.println(block.getText());
        }
            
        Assert.isTrue(secondPage.isLast(), "This page must be last");

    }

    @Test
    public void deleteNewsContentTest() {
        if(env.getNewsContentRepository().countByNews(env.getTestNews()) == 0) {
            for(long i = 0; i < blocksPerPage + 1; i++) {
                NewsContentBlock block = new NewsContentBlock();
                block.setText("Block" + i);
                block.setNewsContentBlockId(new NewsContentBlockId(null, i));
                block.setNews(env.getTestNews());

                block = env.getNewsContentService().saveNewsContentBlock(block);
            }
        }
            
        Assert.isTrue(env.getNewsContentRepository().countByNews(env.getTestNews()) > 0, "There must be something to delete");

        env.getNewsContentService().deleteAllContentOfNews(env.getTestNews());

        Assert.isTrue(env.getNewsContentRepository().countByNews(env.getTestNews()) == 0, "There must be no content");

        Assert.isTrue(env.getNewsRepository().existsById(env.getTestNews().getId()), "News must remain present");
    }
}
