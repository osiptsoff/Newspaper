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

    public Page<Comment> findNthPageOfCommentsByNewsId(Integer newsId, Integer page) {
        log.info("Got request for comments; page = " + page + " , news id = " + newsId);

        try {
            Page<Comment> result = commentRepository
                    .findByNewsId(newsId, PageRequest.of(page, commentPageSize));

            log.info("Successfully got " + result.getNumberOfElements() + " comments");

            return result;
        } catch(Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }

    public Page<Comment> findNthPageOfCommentsByNews(News news, Integer page) {
        return findNthPageOfCommentsByNewsId(news.getId(), page);
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

    public String getLoginOfAuthor(Integer newsId) {
        log.info("Got request for login of user who authored comment witd id = " + newsId);

        try {
            String result = commentRepository.getAuthorLogin(newsId);

            log.info("Successfully got login of user who authored comment witd id = " + newsId);

            return result;
        } catch(Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }
}
