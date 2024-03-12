package ru.spb.nicetu.newspaper.api.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.spb.nicetu.newspaper.api.dto.CommentDto;
import ru.spb.nicetu.newspaper.api.dto.PageDto;
import ru.spb.nicetu.newspaper.api.service.facade.CommentsServiceFacade;

/**
 * <p>Controller for '/comment' endpoint.</p>
 *
 * <p>Provides API for posting, updating, deletion and reading comments.</p>
    * @author Nikita Osiptsov
    * @see {@link CommentsServiceFacade}
 * @since 1.0
 */
@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
@Validated
public class CommentsController {
    private final CommentsServiceFacade commentsServiceFacade;

    @PostMapping()
    public CommentDto saveComment(@Valid @RequestBody CommentDto dto) {
        return commentsServiceFacade.saveComment(dto);
    }

    @PatchMapping
    public CommentDto updateComment(@Valid @RequestBody CommentDto dto) {
        return commentsServiceFacade.updateComment(dto);
    }

    @GetMapping()
    public PageDto<CommentDto> getPage(@RequestParam Long newsId, @RequestParam Integer pageNumber) {
        return commentsServiceFacade.getPage(newsId, pageNumber);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable("id") Long commentId) {
        commentsServiceFacade.deleteComment(commentId);
    }

    @DeleteMapping("/{id}/superuser")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentNoCheck(@PathVariable("id") Long commentId) {
        commentsServiceFacade.deleteCommentNoCheck(commentId);
    }
}
