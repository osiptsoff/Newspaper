package ru.spb.nicetu.newspaper.api.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.spb.nicetu.newspaper.api.dto.LikeNewsDto;
import ru.spb.nicetu.newspaper.api.dto.LikeTagDto;
import ru.spb.nicetu.newspaper.api.dto.NewsSignatureDto;
import ru.spb.nicetu.newspaper.api.dto.PageDto;
import ru.spb.nicetu.newspaper.api.dto.SingleValueDto;
import ru.spb.nicetu.newspaper.api.dto.UserInfoDto;
import ru.spb.nicetu.newspaper.api.dto.UserTagListDto;
import ru.spb.nicetu.newspaper.api.service.UserService;
import ru.spb.nicetu.newspaper.api.service.facade.UserServiceFacade;
import ru.spb.nicetu.newspaper.api.service.util.AuthUtil;

/**
 * <p>Controller for '/user' endpoint.</p>
 *
 * <p>Provides API for: <ol>
 *      <li>getting user information (user's attitude toward tag or news, user's name and lastname),</li>
 *      <li>changing some of this information (liking news, marking tags as liked or disliked),</li>
 *      <li>getting news considering user's preferences.</li></ol></p>
 * <p>All paths of this endpoint have additional protection: user can retrieve information of his own only.</p>
    * @author Nikita Osiptsov
    * @see {@link UserService}
    * @see {@link AuthUtil}
 * @since 1.0
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceFacade userServiceFacade;

    @GetMapping()
    public UserInfoDto getUserInfo() {
        return userServiceFacade.getUserInfo();
    }

    @GetMapping("/tag")
    public UserTagListDto getUserTags() {
        return userServiceFacade.getUserTags();
    }

    @GetMapping("/tag/like")
    public SingleValueDto<Boolean> userLikedTag(@RequestParam String name) {
        return userServiceFacade.userLikedTag(name);
    }

    @GetMapping("/news")
    public PageDto<NewsSignatureDto> getNewsByPreferences(@RequestParam Integer pageNumber) {
        return userServiceFacade.getNewsByPreferences(pageNumber);
    }

    @GetMapping("/news/like")
    public SingleValueDto<Boolean> userLikedNews(@RequestParam Long newsId) {
        return userServiceFacade.userLikedNews(newsId);
    }

    @PostMapping("/news/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void likeNews(@Valid @RequestBody LikeNewsDto dto) {
        userServiceFacade.likeNews(dto);
    }

    @PostMapping("/tag/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void associateWithTag(@Valid @RequestBody LikeTagDto dto) {
        userServiceFacade.associateWithTag(dto);
    }
}
