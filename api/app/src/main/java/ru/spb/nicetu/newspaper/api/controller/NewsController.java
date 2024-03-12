package ru.spb.nicetu.newspaper.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.spb.nicetu.newspaper.api.dto.FetchedNewsDto;
import ru.spb.nicetu.newspaper.api.dto.NewsContentBlockDto;
import ru.spb.nicetu.newspaper.api.dto.NewsSignatureDto;
import ru.spb.nicetu.newspaper.api.dto.PageDto;
import ru.spb.nicetu.newspaper.api.dto.TagAssociationRequestDto;
import ru.spb.nicetu.newspaper.api.service.NewsContentService;
import ru.spb.nicetu.newspaper.api.service.NewsService;
import ru.spb.nicetu.newspaper.api.service.facade.NewsServiceFacade;

/**
 * <p>Controller for '/news' endpoint.</p>
 *
 * <p>Provides API for posting, updating, deletion and reading news and news content.</p>
    * @author Nikita Osiptsov
    * @see {@link NewsService}
    * @see {@link NewsContentService}
 * @since 1.0
 */
@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
@Validated
public class NewsController {
    private final NewsServiceFacade newsServiceFacade;

    @GetMapping()
    public PageDto<NewsSignatureDto> getAllNewsPage(@RequestParam Integer pageNumber) {
        return newsServiceFacade.getAllNewsPage(pageNumber);
    }

    @GetMapping("/{id}")
    public FetchedNewsDto getNews(@PathVariable Long id) {
        return newsServiceFacade.getNews(id);
    }

    @GetMapping("{id}/content")
    public PageDto<NewsContentBlockDto> getNewsContentPage(@PathVariable("id") Long newsId,
            @RequestParam Integer pageNumber) {
        return newsServiceFacade.getNewsContentPage(newsId, pageNumber);
    }

    @PostMapping()
    public NewsSignatureDto saveNewsSignature(@Valid @RequestBody NewsSignatureDto dto) {
        return newsServiceFacade.saveNewsSignature(dto);
    }

    @PostMapping("/{id}/content")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void saveNewsContent(@Valid @RequestBody List<NewsContentBlockDto> blocks,
            @PathVariable Long id) {
        newsServiceFacade.saveNewsContent(blocks, id);
    }

    @PostMapping("/{id}/tag")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void associateWithTag(@Valid @RequestBody TagAssociationRequestDto dto,
            @PathVariable Long id) {
        newsServiceFacade.associateWithTag(dto, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNews(@PathVariable("id") Long newsId) {
        newsServiceFacade.deleteNews(newsId);
    }

    @DeleteMapping("/{id}/content")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNewsContent(@PathVariable Long id) {
        newsServiceFacade.deleteNewsContent(id);
    }

}
