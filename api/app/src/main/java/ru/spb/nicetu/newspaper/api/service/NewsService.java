package ru.spb.nicetu.newspaper.api.service;

import org.springframework.data.domain.Page;

import ru.spb.nicetu.newspaper.api.model.Comment;
import ru.spb.nicetu.newspaper.api.model.News;
import ru.spb.nicetu.newspaper.api.model.Tag;
import ru.spb.nicetu.newspaper.api.service.auxiliary.NewsServiceFindNewsByIdResult;

/**
 * <p>Service which encapsulates business logic for news.</p>
 *
 * <p>Can be used to perform CRUD operations on {@code News}s as well as associate {@code News} with {@code Tag}s.</p>
 * <p>Multiple news are returned by pages.</p>
    * @author Nikita Osiptsov
    * @see {@link News}
    * @see {@link Tag}
    * @see {@link Comment}
 * @since 1.2
 */
public interface NewsService {
    Page<News> findAllNews(Integer page);
    News saveNews(News news);
    News findNewsByIdNoFetch(Long id);
    NewsServiceFindNewsByIdResult findNewsById(Long id);
    void associateWithTag(Long newsId, String tagName);
    void deassociateWithTag(Long newsId, String tagName);
    Long countLikes(Long newsId);
    void deleteNews(Long newsId);  
    void deleteNews(News news); 
}
