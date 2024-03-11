package ru.spb.nicetu.newspaper.api.service.facade;

import ru.spb.nicetu.newspaper.api.dto.CommentDto;
import ru.spb.nicetu.newspaper.api.dto.PageDto;
import ru.spb.nicetu.newspaper.api.service.CommentsService;

/**
 * <p>Class which wraps {@link CommentsService} and services of related entities in order to provide
 * simplier interface for interaction.</p>
 * 
 * <p>Minimizes client dependencies; converts DTOs to domain objects, converts result domain objects to DTOs</p>
    * @author Nikita Osiptsov
    * @see {@link CommentsService}
 * @since 1.1
 */
public interface CommentsServiceFacade {
    CommentDto saveComment(CommentDto dto);
    CommentDto updateComment(CommentDto dto);
    PageDto<CommentDto> getPage(Long newsId, Integer pageNumber);
    void deleteComment(Long commentId);
    void deleteCommentNoCheck(Long commentId);
}
