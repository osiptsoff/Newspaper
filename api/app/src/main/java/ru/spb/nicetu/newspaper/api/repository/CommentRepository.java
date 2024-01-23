package ru.spb.nicetu.newspaper.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.spb.nicetu.newspaper.api.model.Comment;
import ru.spb.nicetu.newspaper.api.model.News;

/**
 * <p>{@code Comment} repository.</p>
    * @author Nikita Osiptsov
    * @see {@link Comment}
 * @since 1.0
 */
@Repository
public interface CommentRepository extends PagingAndSortingRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c WHERE c.news.id = :id ORDER BY c.postTime DESC")
    Page<Comment> findByNewsId(@Param("id") Long newsId, Pageable pageable);
    Page<Comment> findAllByNewsOrderByPostTimeDesc(News news, Pageable pageable);

    @Query("SELECT c.author.login FROM Comment c WHERE c.id = :id")
    String getAuthorLogin(@Param("id") Long commentId);

    Long deleteAllByNews(News news);
}
