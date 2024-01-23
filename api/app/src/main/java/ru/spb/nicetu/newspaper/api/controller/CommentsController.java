package ru.spb.nicetu.newspaper.api.controller;

import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.spb.nicetu.newspaper.api.controller.util.AuthUtil;
import ru.spb.nicetu.newspaper.api.dto.CommentDto;
import ru.spb.nicetu.newspaper.api.dto.IdDto;
import ru.spb.nicetu.newspaper.api.dto.PageDto;
import ru.spb.nicetu.newspaper.api.dto.PageRequestDto;
import ru.spb.nicetu.newspaper.api.model.Comment;
import ru.spb.nicetu.newspaper.api.model.News;
import ru.spb.nicetu.newspaper.api.model.User;
import ru.spb.nicetu.newspaper.api.service.CommentsService;
import ru.spb.nicetu.newspaper.api.service.NewsService;
import ru.spb.nicetu.newspaper.api.service.UserService;
import ru.spb.nicetu.newspaper.api.service.exception.MissingEntityException;

/**
 * <p>Controller for '/comment' endpoint.</p>
 *
 * <p>Provides API for posting, updating, deletion and reading comments.</p>
    * @author Nikita Osiptsov
    * @see {@link CommentsService}
 * @since 1.0
 */
@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
@Validated
public class CommentsController {
    private final CommentsService commentsService;
    private final UserService userService;
    private final NewsService newsService;

    private final AuthUtil authUtil;

    @PostMapping()
    public CommentDto saveComment(@Valid @RequestBody CommentDto dto) {
        News news = newsService.findNewsByIdNoFetch(dto.getNewsId());
        if(news == null) {
            throw new MissingEntityException();
        }

        User user = userService.getAuthenticatedUser();

        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setNews(news);
        comment.setPostTime(new Date().toInstant().atOffset(ZoneOffset.UTC));
        comment.setText(dto.getText());

        comment = commentsService.saveComment(comment);


        return CommentDto.from(comment);
    }

    @PatchMapping
    public CommentDto updateComment(@Valid @RequestBody CommentDto dto) {
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

    @GetMapping()
    public PageDto<CommentDto> getPage(@Valid @RequestBody PageRequestDto dto) {
        Page<Comment> page = commentsService
            .findNthPageOfCommentsByNewsId(dto.getOwnerId(), dto.getPageNumber());

        List<CommentDto> data = page.map(c -> CommentDto.from(c) ).toList();

        return new PageDto<CommentDto>(data, page.isLast());
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@Valid @RequestBody IdDto dto) {
        String authorLogin = commentsService.getLoginOfAuthor(dto.getId());
        authUtil.checkIfAuthenticated(authorLogin);

        commentsService.deleteComment(dto.getId());
    }

    @DeleteMapping("/superuser")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentNoCheck(@Valid @RequestBody IdDto dto) {
        commentsService.deleteComment(dto.getId());
    }
}
