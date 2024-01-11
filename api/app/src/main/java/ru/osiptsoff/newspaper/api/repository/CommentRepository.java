package ru.osiptsoff.newspaper.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.osiptsoff.newspaper.api.model.Comment;
import ru.osiptsoff.newspaper.api.model.News;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer>{
    Page<Comment> findAllByNewsOrderByPostTimeDesc(News news, Pageable pageable);

    Long deleteAllByNews(News news);
}
