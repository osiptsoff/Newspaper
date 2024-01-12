package ru.osiptsoff.newspaper.api.controller;

import java.time.ZoneOffset;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.osiptsoff.newspaper.api.controller.util.AuthUtil;
import ru.osiptsoff.newspaper.api.dto.CommentDto;
import ru.osiptsoff.newspaper.api.dto.PageRequestDto;
import ru.osiptsoff.newspaper.api.dto.IdDto;
import ru.osiptsoff.newspaper.api.model.Comment;
import ru.osiptsoff.newspaper.api.model.News;
import ru.osiptsoff.newspaper.api.model.User;
import ru.osiptsoff.newspaper.api.service.CommentsService;
import ru.osiptsoff.newspaper.api.service.NewsService;
import ru.osiptsoff.newspaper.api.service.UserService;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentsController {
    private final CommentsService commentsService;
    private final UserService userService;
    private final NewsService newsService;

    private final AuthUtil authUtil;

    @PostMapping()
    public CommentDto saveComment(@RequestBody CommentDto dto) {
        checkIfEmpty(dto.getText());

        News news = newsService.findNewsByIdNoFetch(dto.getNewsId());
        User user = userService.getAuthenticatedUser();

        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setNews(news);
        comment.setPostTime(new Date().toInstant().atOffset(ZoneOffset.UTC));
        comment.setText(dto.getText());

        commentsService.saveComment(comment);

        dto.setId(comment.getId());
        dto.setAuthor(user.getLogin());
        dto.setPostTime(comment.getPostTime());

        return dto;
    }

    @PatchMapping
    public CommentDto updateComment(@RequestBody CommentDto dto) {
        checkIfEmpty(dto.getText());

        String authorLogin = commentsService.getLoginOfAuthor(dto.getId());
        authUtil.checkIfAuthenticated(authorLogin);

        Comment comment = commentsService.findCommentById(dto.getId());
        if(comment == null)
            throw new NullPointerException();

        comment.setText(dto.getText());
        comment.setPostTime(new Date().toInstant().atOffset(ZoneOffset.UTC));
        commentsService.saveComment(comment);

        dto.setPostTime(comment.getPostTime());

       return dto;
    }

    @GetMapping()
    public List<CommentDto> getPage(@RequestBody PageRequestDto dto) {
        List<CommentDto> result = new LinkedList<>();
        Page<Comment> page = commentsService.findNthPageOfCommentsByNewsId(dto.getOwnerId(), dto.getPageNumber());

        page.forEach(c -> result.add( CommentDto.from(c) ));

        return result;
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@RequestBody IdDto dto) {
        String authorLogin = commentsService.getLoginOfAuthor(dto.getId());
        authUtil.checkIfAuthenticated(authorLogin);

        commentsService.deleteComment(dto.getId());
    }

    @DeleteMapping("/superuser")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentNoCheck(@RequestBody IdDto dto) {
        commentsService.deleteComment(dto.getId());
    }

    private void checkIfEmpty(String string) throws IllegalArgumentException {
        if( string == null || string.trim().isEmpty() )
            throw new IllegalArgumentException();
    }
}
