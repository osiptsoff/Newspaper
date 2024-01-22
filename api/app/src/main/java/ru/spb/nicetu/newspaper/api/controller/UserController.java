package ru.spb.nicetu.newspaper.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.spb.nicetu.newspaper.api.controller.util.AuthUtil;
import ru.spb.nicetu.newspaper.api.dto.IdDto;
import ru.spb.nicetu.newspaper.api.dto.LikeNewsDto;
import ru.spb.nicetu.newspaper.api.dto.LikeTagDto;
import ru.spb.nicetu.newspaper.api.dto.NewsSignatureDto;
import ru.spb.nicetu.newspaper.api.dto.PageDto;
import ru.spb.nicetu.newspaper.api.dto.PageRequestDtoNoOwner;
import ru.spb.nicetu.newspaper.api.dto.SingleValueDto;
import ru.spb.nicetu.newspaper.api.dto.TagDto;
import ru.spb.nicetu.newspaper.api.dto.UserInfoDto;
import ru.spb.nicetu.newspaper.api.dto.UserTagListDto;
import ru.spb.nicetu.newspaper.api.model.News;
import ru.spb.nicetu.newspaper.api.model.User;
import ru.spb.nicetu.newspaper.api.service.UserService;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    private final AuthUtil authUtil;

    @GetMapping()
    public UserInfoDto getUserInfo() {
        return UserInfoDto.from(userService.getAuthenticatedUser());
    }

    @GetMapping("/tag")
    public UserTagListDto getUserTags() {
        User user = userService.findByLoginFetchTags(authUtil.getAuthenticatedUserName());

        UserTagListDto result = new UserTagListDto();
        result.setLiked(user
            .getLikedTags()
            .stream()
            .map(t -> TagDto.from(t))
            .collect(Collectors.toList()));
        result.setDisliked(user
            .getDislikedTags()
            .stream()
            .map(t -> TagDto.from(t))
            .collect(Collectors.toList()));

        return result;
    }

    @GetMapping("/tag/like")
    public SingleValueDto<Boolean> userLikedTag(@Valid @RequestBody TagDto dto) {
        SingleValueDto<Boolean> result = new SingleValueDto<>();
        result.setValue(userService.isTagLiked(authUtil.getAuthenticatedUserName(), dto.getName()));

        return result;
    }

    @GetMapping("/news")
    public PageDto<NewsSignatureDto> getNewsByPreferences(@Valid @RequestBody PageRequestDtoNoOwner dto) {
        Page<News> page = userService.findPreferredNewsByLogin(
            authUtil.getAuthenticatedUserName(),
            dto.getPageNumber()
        );

        List<NewsSignatureDto> resultList = page.map(n -> NewsSignatureDto.from(n)).toList();

        return new PageDto<NewsSignatureDto>(resultList, page.isLast());
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

        if(dto.getLiked() == true) {
            userService.likeNews(login, dto.getNewsId());
        } else {
            userService.undoLikeNews(login, dto.getNewsId());
        }
    }

    @PostMapping("/tag/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void associateWithTag(@Valid @RequestBody LikeTagDto dto) {
        String login = authUtil.getAuthenticatedUserName();

        if(dto.getLiked() == null) {
            userService.undoTagAssociation(login, dto.getName());
        } else if(dto.getLiked() == true) {
            userService.likeTag(login, dto.getName());
        } else {
            userService.dislikeTag(login, dto.getName());
        }
    }
}
