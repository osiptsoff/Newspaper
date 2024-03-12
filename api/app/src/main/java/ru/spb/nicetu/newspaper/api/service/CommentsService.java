package ru.spb.nicetu.newspaper.api.service;

import org.springframework.data.domain.Page;

import ru.spb.nicetu.newspaper.api.model.Comment;
import ru.spb.nicetu.newspaper.api.model.News;

/**
 * <p>Service which encapsulates business logic for comments.</p>
 *
 * <p>Can be used to perform CRUD operations on {@code Comment}s.</p>
 * <p>Multiple comments are returned by pages.</p>
 * <p>Logs its work and unpredicted exceptions.</p>
    * @author Nikita Osiptsov
    * @see {@link Comment}
 * @since 1.2
 */
public interface CommentsService {
    Comment saveComment(Comment comment);
    Comment findCommentById(Long id);
    Page<Comment> findNthPageOfCommentsByNewsId(Long newsId, Integer page);
    Page<Comment> findNthPageOfCommentsByNews(News news, Integer page);
    void deleteComment(Long id);
    void deleteComment(Comment comment);
    String getLoginOfAuthor(Long newsId);
}
