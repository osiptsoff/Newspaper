package ru.spb.nicetu.newspaper.api.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.spb.nicetu.newspaper.api.model.Comment;
import ru.spb.nicetu.newspaper.api.model.News;
import ru.spb.nicetu.newspaper.api.repository.CommentRepository;
import ru.spb.nicetu.newspaper.api.service.exception.MissingEntityException;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * <p>{@link CommentsService} implementation.</p>
 *
 * <p>Multiple comments are returned by pages, size of page can be specified through application properties.</p>
 * <p>Logs its work and unpredicted exceptions.</p>
    * @author Nikita Osiptsov
    * @see {@link Comment}
    * @see {@link CommentRepository}
 * @since 1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CommentsServiceImpl implements CommentsService {
    private final CommentRepository commentRepository;

    @Value("${app.config.commentPageSize}")
    @Setter
    private Integer commentPageSize;

    @Override
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

    @Override
    public Comment findCommentById(Long id) {
        log.info("Got request for comment with id = " + id);

        try {
            Optional<Comment> res = commentRepository.findById(id);
            if(!res.isPresent()) {
                log.info("No comment with id = " + id + " present");
                return null;
            }
            Comment comment = res.get();

            log.info("Succesfully got comment with id = " + id);

            return comment;
        } catch(Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }

    @Override
    public Page<Comment> findNthPageOfCommentsByNewsId(Long newsId, Integer page) {
        log.info("Got request for comments; page = " + page + " , news id = " + newsId);

        try {
            Page<Comment> result = commentRepository
                .findByNewsId(newsId, PageRequest.of(page, commentPageSize));

            log.info("Got " + result.getNumberOfElements() + " comments");

            return result;
        } catch(Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }

    @Override
    public Page<Comment> findNthPageOfCommentsByNews(News news, Integer page) {
        return findNthPageOfCommentsByNewsId(news.getId(), page);
    }

    @Override
    public void deleteComment(Long id) {
        log.info("Got request to delete comment with id = " + id);

        try {
            if(!commentRepository.existsById(id)) {
                throw new MissingEntityException();
            }
                
            commentRepository.deleteById(id);

            log.info("Successfully deleted comment, id = " + id);
        } catch(MissingEntityException e) {
            log.info("Unsuccessful delete: entity does not exist");
            throw e;
        } catch(Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }

    @Override
    public void deleteComment(Comment comment) {
        deleteComment(comment.getId());
    }

    @Override
    public String getLoginOfAuthor(Long newsId) {
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
