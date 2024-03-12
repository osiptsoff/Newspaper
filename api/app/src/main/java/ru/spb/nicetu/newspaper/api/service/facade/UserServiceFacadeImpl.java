package ru.spb.nicetu.newspaper.api.service.facade;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ru.spb.nicetu.newspaper.api.dto.LikeNewsDto;
import ru.spb.nicetu.newspaper.api.dto.LikeTagDto;
import ru.spb.nicetu.newspaper.api.dto.NewsSignatureDto;
import ru.spb.nicetu.newspaper.api.dto.PageDto;
import ru.spb.nicetu.newspaper.api.dto.SingleValueDto;
import ru.spb.nicetu.newspaper.api.dto.TagDto;
import ru.spb.nicetu.newspaper.api.dto.UserInfoDto;
import ru.spb.nicetu.newspaper.api.dto.UserTagListDto;
import ru.spb.nicetu.newspaper.api.model.News;
import ru.spb.nicetu.newspaper.api.model.User;
import ru.spb.nicetu.newspaper.api.service.SecurityUserService;
import ru.spb.nicetu.newspaper.api.service.UserService;
import ru.spb.nicetu.newspaper.api.service.util.AuthUtil;

/**
 * <p>{@link UserService} implementation.</p>
 * 
    * @author Nikita Osiptsov
    * @see {@link UserService}
    * @see {@link AuthUtil}
    * @see {@link SecurityUserService}
 * @since 1.2
 */
@Component
@RequiredArgsConstructor
public class UserServiceFacadeImpl implements UserServiceFacade {
    private final UserService userService;

    private final AuthUtil authUtil;
    private final SecurityUserService securityUserService;

    @Override
    public UserInfoDto getUserInfo() {
        return UserInfoDto.from(securityUserService.getAuthenticatedUser());
    }

    @Override
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

    @Override
    public SingleValueDto<Boolean> userLikedTag(String name) {
        SingleValueDto<Boolean> result = new SingleValueDto<>();
        result.setValue(userService.isTagLiked(authUtil.getAuthenticatedUserName(), name));

        return result;
    }

    @Override
    public PageDto<NewsSignatureDto> getNewsByPreferences(Integer pageNumber) {
         Page<News> page = userService.findPreferredNewsByLogin(
            authUtil.getAuthenticatedUserName(),
            pageNumber
        );

        List<NewsSignatureDto> resultList = page.map(n -> NewsSignatureDto.from(n)).toList();

        return new PageDto<NewsSignatureDto>(resultList, page.isLast());
    }

    @Override
    public SingleValueDto<Boolean> userLikedNews(Long newsId) {
        SingleValueDto<Boolean> result = new SingleValueDto<>();
        result.setValue(userService.isNewsLiked(authUtil.getAuthenticatedUserName(), newsId));

        return result;
    }

    @Override
    public void likeNews(LikeNewsDto dto) {
        String login = authUtil.getAuthenticatedUserName();

        if(dto.getLiked() == true) {
            userService.likeNews(login, dto.getNewsId());
        } else {
            userService.undoLikeNews(login, dto.getNewsId());
        }
    }

    @Override
    public void associateWithTag(LikeTagDto dto) {
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
