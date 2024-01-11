package ru.osiptsoff.newspaper.api.controller;

import java.time.ZoneOffset;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.osiptsoff.newspaper.api.dto.CommentDto;
import ru.osiptsoff.newspaper.api.dto.CommentPageRequestDto;
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

    @PostMapping()
    public CommentDto saveComment(@RequestBody CommentDto dto) {
        News news = newsService.findNewsByIdNoFetch(dto.getNewsId());
        User user = userService.getAuthenticatedUser();

        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setNews(news);
        comment.setPostTime(new Date().toInstant().atOffset(ZoneOffset.UTC));
        comment.setId(dto.getId());
        comment.setText(dto.getText());

        commentsService.saveComment(comment);

        dto.setAuthor(user.getLogin());
        dto.setPostTime(comment.getPostTime());

        return dto;
    }

    @GetMapping()
    public List<CommentDto> getPage(@RequestBody CommentPageRequestDto dto) {
        List<CommentDto> result = new LinkedList<>();

        Page<Comment> page = commentsService.findNthPageOfCommentsByNewsId(dto.getNewsId(), dto.getPageNumber());
    
        page.stream().forEach(c -> result.add( CommentDto.from(c) ));

        return result;
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@RequestBody IdDto dto) {
        String authorLogin = commentsService.getLoginOfAuthor(dto.getId());
        String issuerLogin = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        if(!authorLogin.equals(issuerLogin)) {
            throw new BadCredentialsException("Unauthorized attempt to delete comment");
        }

        commentsService.deleteComment(dto.getId());
    }

    @DeleteMapping("/superuser")
    public void deleteCommentNoCheck(@RequestBody IdDto dto) {
        commentsService.deleteComment(dto.getId());
    }
}
