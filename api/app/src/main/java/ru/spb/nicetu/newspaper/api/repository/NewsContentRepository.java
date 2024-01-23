package ru.spb.nicetu.newspaper.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.spb.nicetu.newspaper.api.model.News;
import ru.spb.nicetu.newspaper.api.model.NewsContentBlock;
import ru.spb.nicetu.newspaper.api.model.embeddable.NewsContentBlockId;

/**
 * <p>{@code NewsContentBlock} repository.</p>
    * @author Nikita Osiptsov
    * @see {@link NewsContentBlock}
 * @since 1.0
 */
@Repository
public interface NewsContentRepository extends PagingAndSortingRepository<NewsContentBlock, NewsContentBlockId> {
    @Query("SELECT b FROM NewsContentBlock b "
        + "WHERE b.newsContentBlockId.newsId = :id "
        + "ORDER BY b.newsContentBlockId.number ASC ")
    Page<NewsContentBlock> findByNewsId(@Param("id") Long newsId, Pageable pageable);

    Long countByNews(News news);

    Long deleteByNews(News news);

}
