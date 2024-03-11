package ru.spb.nicetu.newspaper.api.service;

import java.util.List;

import org.springframework.data.domain.Page;

import ru.spb.nicetu.newspaper.api.model.News;
import ru.spb.nicetu.newspaper.api.model.NewsContentBlock;

/**
 * <p>Service which encapsulates business logic for news content.</p>
 *
 * <p>Can be used to perform CRUD operations on {@code NewsContentBlock}s.</p>
 * <p>Multiple blocks are returned by pages.</p>
    * @author Nikita Osiptsov
    * @see {@link NewsContentBlock}
 * @since 1.0
 */
public interface NewsContentService {
    NewsContentBlock saveNewsContentBlock(NewsContentBlock newsContentBlock);
    List<NewsContentBlock> saveMultipleNewsContentBlocks(List<NewsContentBlock> blocks);
    Page<NewsContentBlock> findNthPageOfContent(Long newsId, Integer page);
    void deleteAllContentOfNews(News news);
}
