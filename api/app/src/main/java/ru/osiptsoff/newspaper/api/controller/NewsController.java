package ru.osiptsoff.newspaper.api.controller;

import java.time.ZoneOffset;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.osiptsoff.newspaper.api.dto.CommentDto;
import ru.osiptsoff.newspaper.api.dto.FetchedNewsDto;
import ru.osiptsoff.newspaper.api.dto.IdDto;
import ru.osiptsoff.newspaper.api.dto.NewsContentBlockDto;
import ru.osiptsoff.newspaper.api.dto.NewsSignatureDto;
import ru.osiptsoff.newspaper.api.dto.PageDto;
import ru.osiptsoff.newspaper.api.dto.PageRequestDto;
import ru.osiptsoff.newspaper.api.dto.PageRequestDtoNoOwner;
import ru.osiptsoff.newspaper.api.dto.TagAssociationRequestDto;
import ru.osiptsoff.newspaper.api.model.News;
import ru.osiptsoff.newspaper.api.model.NewsContentBlock;
import ru.osiptsoff.newspaper.api.model.embeddable.NewsContentBlockId;
import ru.osiptsoff.newspaper.api.service.NewsContentService;
import ru.osiptsoff.newspaper.api.service.NewsService;
import ru.osiptsoff.newspaper.api.service.auxiliary.NewsServiceFindNewsByIdResult;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
@Validated
public class NewsController {
    private final NewsService newsService;
    private final NewsContentService newsContentService;

    @GetMapping()
    public PageDto<NewsSignatureDto> getAllNewsPage(@Valid @RequestBody PageRequestDtoNoOwner dto) {
        Page<News> page = newsService.findAllNews(dto.getPageNumber());

        List<NewsSignatureDto> resultList = page.map( n -> NewsSignatureDto.from(n) ).toList();

        return new PageDto<NewsSignatureDto>(resultList, page.isLast());
    }

    @GetMapping("/{id}")
    public FetchedNewsDto getNews(@PathVariable Long id) {
        NewsServiceFindNewsByIdResult fetchedNews = newsService.findNewsById(id);
        if(fetchedNews == null)
            throw new NullPointerException();

        FetchedNewsDto result = new FetchedNewsDto();
        result.setNewsSignature( NewsSignatureDto.from(fetchedNews.getNews()) );
        result.setComments( fetchedNews
                            .getNews()
                            .getComments()
                            .stream()
                            .map( c -> CommentDto.from(c) )
                            .collect(Collectors.toList())
                        );
        result.setContent(  fetchedNews
                            .getNews()
                            .getContent()
                            .stream()
                            .map( b -> NewsContentBlockDto.from(b) )
                            .collect(Collectors.toList())  
                        );

        result.setLikesCount(newsService.countLikes(id));

        result.setIsLastCommentsPage( fetchedNews.getIsLastCommentsPage() );
        result.setIsLastContentPage( fetchedNews.getIsLastContentPage() );

        return result;
    }

    @GetMapping("/content")
    public PageDto<NewsContentBlockDto> getNewsContentPage(@Valid @RequestBody PageRequestDto dto) {
        List<NewsContentBlockDto> data = new LinkedList<>();
        Page<NewsContentBlock> page = newsContentService.findNthPageOfContent(dto.getOwnerId(), dto.getPageNumber());

        page.forEach( b -> data.add( NewsContentBlockDto.from(b) ));

        return new PageDto<NewsContentBlockDto>(data, page.isLast());
    }

    @PostMapping()
    public NewsSignatureDto saveNewsSignature(@Valid @RequestBody NewsSignatureDto dto) {
        News newsSignature = new News();
        newsSignature.setId(dto.getId());
        newsSignature.setTitle(dto.getTitle());
        newsSignature.setPostTime(new Date().toInstant().atOffset(ZoneOffset.UTC));
        newsSignature = newsService.saveNews(newsSignature);

        dto.setId(newsSignature.getId());
        dto.setPostTime(newsSignature.getPostTime());
        dto.setTags(null);

        return dto;
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void saveNewsContent(@Valid @RequestBody List<NewsContentBlockDto> blocks,
                                @PathVariable Long id) {
        News news = newsService.findNewsByIdNoFetch(id);
        if(news == null)
            throw new NullPointerException();
        List<NewsContentBlock> result = new LinkedList<>();

        blocks.forEach( b -> {
            NewsContentBlock block = new NewsContentBlock();
            block.setNewsContentBlockId( new NewsContentBlockId() );
            block.getNewsContentBlockId().setNewsId(news.getId());
            block.getNewsContentBlockId().setNumber(b.getBlockNumber());
            block.setNews(news);
            block.setText(b.getText());

            result.add(block);
        });

        newsContentService.saveMultipleNewsContentBlocks(result);
    }

    @PostMapping("/{id}/tag")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void associateWithTag(@Valid @RequestBody TagAssociationRequestDto dto,
                                 @PathVariable Long id) {
        if(dto.getBelongs())
            newsService.associateWithTag(id, dto.getTag());
        else
            newsService.deassociateWithTag(id, dto.getTag());
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNews(@Valid @RequestBody IdDto idDto) {
        newsService.deleteNews(idDto.getId());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNewsContent(@PathVariable Long id) {
        News news = newsService.findNewsByIdNoFetch(id);

        newsContentService.deleteAllContentOfNews(news);
    }

}
