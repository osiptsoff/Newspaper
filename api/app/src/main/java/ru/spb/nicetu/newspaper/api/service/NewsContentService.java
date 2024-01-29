package ru.spb.nicetu.newspaper.api.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.spb.nicetu.newspaper.api.model.News;
import ru.spb.nicetu.newspaper.api.model.NewsContentBlock;
import ru.spb.nicetu.newspaper.api.repository.NewsContentRepository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * <p>Service which encapsulates business logic for news content.</p>
 *
 * <p>Can be used to perform CRUD operations on {@code NewsContentBlock}s.</p>
 * <p>Multiple blocks are returned by pages, size of page can be specified through application properties.</p>
 * <p>Logs its work and unpredicted exceptions.</p>
    * @author Nikita Osiptsov
    * @see {@link NewsContentBlock}
    * @see {@link NewsContentRepository}
 * @since 1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class NewsContentService {
    private final NewsContentRepository newsContentRepository;

    @Value("${app.config.textBlockPageSize}")
    @Setter
    private Integer textBlockPageSize;

    public NewsContentBlock saveNewsContentBlock(NewsContentBlock newsContentBlock) {
        log.info("Got request to save news content block");

        try {
            NewsContentBlock result = newsContentRepository.save(newsContentBlock);

            log.info("Successfully saved block, key: " + result.getNewsContentBlockId());

            return result;
        } catch(Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }

    public List<NewsContentBlock> saveMultipleNewsContentBlocks(List<NewsContentBlock> blocks) {
        blocks.forEach(b -> b = saveNewsContentBlock(b));

        return blocks;
    }

    public Page<NewsContentBlock> findNthPageOfContent(Long newsId, Integer page) {
        log.info("Got request for news content; page = " + page + ", news id = " + newsId);

        try {
            Page<NewsContentBlock> result = newsContentRepository
                .findByNewsId(newsId, PageRequest.of(page, textBlockPageSize));

            log.info("Successfully got " + result.getNumberOfElements() + " blocks");

            return result;
        } catch(Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }

    public void deleteAllContentOfNews(News news) {
        log.info("Got request to delete content of news with id = " + news.getId());

        try {
            newsContentRepository.deleteByNews(news);

            log.info("Successfully deleted content of news with id = " + news.getId());
        } catch(Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }
}
