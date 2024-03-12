package ru.spb.nicetu.newspaper.api.service.facade;

import ru.spb.nicetu.newspaper.api.dto.LikeNewsDto;
import ru.spb.nicetu.newspaper.api.dto.LikeTagDto;
import ru.spb.nicetu.newspaper.api.dto.NewsSignatureDto;
import ru.spb.nicetu.newspaper.api.dto.PageDto;
import ru.spb.nicetu.newspaper.api.dto.SingleValueDto;
import ru.spb.nicetu.newspaper.api.dto.UserInfoDto;
import ru.spb.nicetu.newspaper.api.dto.UserTagListDto;
import ru.spb.nicetu.newspaper.api.service.UserService;

/**
 * <p>Class which wraps {@link UserService} and services of related entities in order to provide
 * simplier interface for interaction.</p>
 * 
 * <p>Minimizes client dependencies; converts DTOs to domain objects, converts result domain objects to DTOs</p>
    * @author Nikita Osiptsov
    * @see {@link UserService}
 * @since 1.2
 */
public interface UserServiceFacade {
    UserInfoDto getUserInfo();
    UserTagListDto getUserTags();
    SingleValueDto<Boolean> userLikedTag(String name);
    PageDto<NewsSignatureDto> getNewsByPreferences(Integer pageNumber);
    SingleValueDto<Boolean> userLikedNews(Long newsId);
    void likeNews(LikeNewsDto dto);
    void associateWithTag(LikeTagDto dto);
}
