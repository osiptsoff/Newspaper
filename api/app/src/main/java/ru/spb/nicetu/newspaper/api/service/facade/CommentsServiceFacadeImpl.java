package ru.spb.nicetu.newspaper.api.service.facade;

import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ru.spb.nicetu.newspaper.api.dto.CommentDto;
import ru.spb.nicetu.newspaper.api.dto.PageDto;
import ru.spb.nicetu.newspaper.api.model.Comment;
import ru.spb.nicetu.newspaper.api.model.News;
import ru.spb.nicetu.newspaper.api.model.User;
import ru.spb.nicetu.newspaper.api.service.CommentsService;
import ru.spb.nicetu.newspaper.api.service.NewsService;
import ru.spb.nicetu.newspaper.api.service.SecurityUserService;
import ru.spb.nicetu.newspaper.api.service.exception.MissingEntityException;
import ru.spb.nicetu.newspaper.api.service.util.AuthUtil;

/**
 * <p>{@link CommentsServiceFacade} implementation</p>
 * 
    * @author Nikita Osiptsov
    * @see {@link CommentsService}
    * @see {@link NewsService}
    * @see {@link AuthUtil}
    * @see {@link SecurityUserService}
 * @since 1.2
 */
@Component
@RequiredArgsConstructor
public class CommentsServiceFacadeImpl implements CommentsServiceFacade {
    private final CommentsService commentsService;
    private final NewsService newsService;

    private final AuthUtil authUtil;
    private final SecurityUserService securityUserService;

    @Override
    public CommentDto saveComment(CommentDto dto) {
        News news = newsService.findNewsByIdNoFetch(dto.getNewsId());
        if(news == null) {
            throw new MissingEntityException();
        }

        User user = securityUserService.getAuthenticatedUser();

        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setNews(news);
        comment.setPostTime(new Date().toInstant().atOffset(ZoneOffset.UTC));
        comment.setText(dto.getText());

        comment = commentsService.saveComment(comment);


        return CommentDto.from(comment);
    }

    @Override
    public CommentDto updateComment(CommentDto dto) {
        String authorLogin = commentsService.getLoginOfAuthor(dto.getId());
        authUtil.checkIfAuthenticated(authorLogin);

        Comment comment = commentsService.findCommentById(dto.getId());
        if(comment == null) {
            throw new NullPointerException();
        }

        comment.setText(dto.getText());
        comment.setPostTime(new Date().toInstant().atOffset(ZoneOffset.UTC));
        commentsService.saveComment(comment);

       return CommentDto.from(comment);
    }

    @Override
    public PageDto<CommentDto> getPage(Long newsId, Integer pageNumber) {
        Page<Comment> page = commentsService
            .findNthPageOfCommentsByNewsId(newsId, pageNumber);
        List<CommentDto> data = page.map(c -> CommentDto.from(c) ).toList();

        return new PageDto<CommentDto>(data, page.isLast());
    }

    @Override
    public void deleteComment(Long commentId) {
        String authorLogin = commentsService.getLoginOfAuthor(commentId);
        authUtil.checkIfAuthenticated(authorLogin);

        commentsService.deleteComment(commentId);
    }

    @Override
    public void deleteCommentNoCheck(Long commentId) {
        commentsService.deleteComment(commentId);
    }
    
}
