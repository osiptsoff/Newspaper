package ru.osiptsoff.newspaper.api.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.osiptsoff.newspaper.api.controller.util.AuthUtil;
import ru.osiptsoff.newspaper.api.dto.IdDto;
import ru.osiptsoff.newspaper.api.dto.LikeNewsDto;
import ru.osiptsoff.newspaper.api.dto.LikeTagDto;
import ru.osiptsoff.newspaper.api.dto.SingleValueDto;
import ru.osiptsoff.newspaper.api.dto.TagDto;
import ru.osiptsoff.newspaper.api.service.UserService;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    private final AuthUtil authUtil;

    @GetMapping("/tag/like")
    public SingleValueDto<Boolean> userLikedTag(@Valid @RequestBody TagDto dto) {
        SingleValueDto<Boolean> result = new SingleValueDto<>();
        result.setValue(userService.isTagLiked(authUtil.getAuthenticatedUserName(), dto.getName()));

        return result;
    }

    @GetMapping("/news/like")
    public SingleValueDto<Boolean> userLikedNews(@Valid @RequestBody IdDto dto) {
        SingleValueDto<Boolean> result = new SingleValueDto<>();
        result.setValue(userService.isNewsLiked(authUtil.getAuthenticatedUserName(), dto.getId()));

        return result;
    }

    @PostMapping("/news/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void likeNews(@Valid @RequestBody LikeNewsDto dto) {
        String login = authUtil.getAuthenticatedUserName();

        if(dto.getLiked() == true)
            userService.likeNews(login, dto.getNewsId());
        else
        userService.undoLikeNews(login, dto.getNewsId());
    }

    @PostMapping("/tag/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void associateWithTag(@Valid @RequestBody LikeTagDto dto) {
        String login = authUtil.getAuthenticatedUserName();

        if(dto.getLiked() == null)
            userService.undoTagAssociation(login, dto.getName());
        else if(dto.getLiked() == true)
            userService.likeTag(login, dto.getName());
        else
            userService.dislikeTag(login, dto.getName());
    }
}
