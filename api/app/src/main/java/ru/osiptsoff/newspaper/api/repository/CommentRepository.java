package ru.osiptsoff.newspaper.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.osiptsoff.newspaper.api.model.Comment;
import ru.osiptsoff.newspaper.api.model.NewsContentBlockId;

@Repository
public interface CommentRepository extends JpaRepository<Comment, NewsContentBlockId>{
    
}
