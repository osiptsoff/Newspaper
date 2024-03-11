package ru.spb.nicetu.newspaper.api.service.facade;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;

import ru.spb.nicetu.newspaper.api.dto.FetchedNewsDto;
import ru.spb.nicetu.newspaper.api.dto.NewsContentBlockDto;
import ru.spb.nicetu.newspaper.api.dto.NewsSignatureDto;
import ru.spb.nicetu.newspaper.api.dto.PageDto;
import ru.spb.nicetu.newspaper.api.dto.TagAssociationRequestDto;
import ru.spb.nicetu.newspaper.api.service.NewsService;

/**
 * <p>Class which wraps {@link NewsService} and services of related entities in order to provide
 * simplier interface for interaction</p>
 * 
 * <p>Minimizes client dependencies; converts DTOs to domain objects, converts result domain objects to DTOs</p>
    * @author Nikita Osiptsov
    * @see {@link NewsService}
 * @since 1.1
 */
public interface NewsServiceFacade {
    PageDto<NewsSignatureDto> getAllNewsPage(Integer pageNumber);
    FetchedNewsDto getNews(@PathVariable Long id);
    PageDto<NewsContentBlockDto> getNewsContentPage(Long newsId, Integer pageNumber);
    NewsSignatureDto saveNewsSignature(NewsSignatureDto dto);
    void saveNewsContent(List<NewsContentBlockDto> blocks, Long id);
    void associateWithTag(TagAssociationRequestDto dto,Long id);
    void deleteNews(Long id);
    void deleteNewsContent(Long id);
}
