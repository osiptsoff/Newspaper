package ru.osiptsoff.newspaper.api.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.osiptsoff.newspaper.api.model.Comment;
import ru.osiptsoff.newspaper.api.model.News;
import ru.osiptsoff.newspaper.api.repository.CommentRepository;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CommentsService {
    private final CommentRepository commentRepository;

    @Value("${app.config.commentPageSize}")
    @Setter
    private Integer commentPageSize;

    public Comment saveComment(Comment comment) {
        log.info("Got request to save comment");

        try {
            Comment result = commentRepository.save(comment);

            log.info("Successfully saved comment, id = " + result.getId());

            return result;
        } catch(Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }

    public Page<Comment> findNthPageOfCommentsByNews(News news, Integer page) {
        log.info("Got request for comments; page = " + page + " , news id = " + news.getId());

        try {
            Page<Comment> result = commentRepository
                    .findAllByNewsOrderByPostTimeDesc(news, PageRequest.of(page, commentPageSize));

            log.info("Successfully got " + result.getNumberOfElements() + " comments");

            return result;
        } catch(Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }

    public void deleteComment(Integer id) {
        log.info("Got request to delete comment with id = " + id);

        try {
            commentRepository.deleteById(id);

            log.info("Successfully deleted comment, id = " + id);
        } catch(Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }

    public void deleteComment(Comment comment) {
        deleteComment(comment.getId());
    }
}
