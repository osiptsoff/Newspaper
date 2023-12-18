package ru.osiptsoff.newspaper.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.osiptsoff.newspaper.api.model.News;
import ru.osiptsoff.newspaper.api.model.NewsContentBlock;
import ru.osiptsoff.newspaper.api.model.NewsContentBlockId;

@Repository
public interface NewsContentRepository extends PagingAndSortingRepository<NewsContentBlock, NewsContentBlockId> {
    @Query("SELECT b FROM NewsContentBlock b WHERE b.newsContentBlockId.newsId = :id ORDER BY b.newsContentBlockId.number")
    Page<NewsContentBlock> findByNewsId(@Param("id") Integer newsId, Pageable pageable);

    Long countAllByNews(News news);

    void deleteAllByNews(News news);

}
