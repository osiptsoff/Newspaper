package ru.osiptsoff.newspaper.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ru.osiptsoff.newspaper.api.model.Comment;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Integer>{
    
}
