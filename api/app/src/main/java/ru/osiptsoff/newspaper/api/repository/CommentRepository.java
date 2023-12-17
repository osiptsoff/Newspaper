package ru.osiptsoff.newspaper.api.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.osiptsoff.newspaper.api.model.Comment;
import ru.osiptsoff.newspaper.api.model.News;
import ru.osiptsoff.newspaper.api.model.NewsContentBlockId;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, NewsContentBlockId>{
    List<Comment> findAllByNewsOrderByPostTimeDesc(News news,  Pageable pageable);
}
