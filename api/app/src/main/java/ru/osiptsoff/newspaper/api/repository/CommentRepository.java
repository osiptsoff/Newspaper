package ru.osiptsoff.newspaper.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.osiptsoff.newspaper.api.model.Comment;
import ru.osiptsoff.newspaper.api.model.News;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query("SELECT c FROM Comment c WHERE c.news.id = :id ORDER BY c.postTime")
    Page<Comment> findByNewsId(@Param("id") Integer newsId, Pageable pageable);
    Page<Comment> findAllByNewsOrderByPostTimeDesc(News news, Pageable pageable);

    @Query("SELECT c.author.login FROM Comment c WHERE c.id = :id")
    String getAuthorLogin(@Param("id") Integer commentId);

    Long deleteAllByNews(News news);
}
