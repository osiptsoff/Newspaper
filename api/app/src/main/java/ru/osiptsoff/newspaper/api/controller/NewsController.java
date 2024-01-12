package ru.osiptsoff.newspaper.api.controller;

import java.time.ZoneOffset;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
import ru.osiptsoff.newspaper.api.dto.PageRequestDto;
import ru.osiptsoff.newspaper.api.dto.TagAssociationRequestDto;
import ru.osiptsoff.newspaper.api.model.News;
import ru.osiptsoff.newspaper.api.model.NewsContentBlock;
import ru.osiptsoff.newspaper.api.service.NewsContentService;
import ru.osiptsoff.newspaper.api.service.NewsService;
import ru.osiptsoff.newspaper.api.service.auxiliary.NewsServiceFindNewsByIdResult;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {
    private final NewsService newsService;
    private final NewsContentService newsContentService;

    @GetMapping()
    public List<NewsSignatureDto> getAllNews() {
        List<NewsSignatureDto> result = new LinkedList<>();
        List<News> news = newsService.findAllNews();

        news.forEach( n -> result.add( NewsSignatureDto.from(n) ));

        return result;
    }

    @GetMapping("/{id}")
    public FetchedNewsDto getNews(@PathVariable Integer id) {
        NewsServiceFindNewsByIdResult fetchedNews = newsService.findNewsById(id);
        if(fetchedNews == null)
            throw new IllegalArgumentException();

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
    public List<NewsContentBlockDto> getNewsContentPage(@RequestBody PageRequestDto dto) {
        List<NewsContentBlockDto> result = new LinkedList<>();
        Page<NewsContentBlock> page = newsContentService.findNthPageOfContent(dto.getOwnerId(), dto.getPageNumber());

        page.forEach( b -> result.add( NewsContentBlockDto.from(b) ));

        return result;
    }

    @PostMapping()
    public NewsSignatureDto saveNewsSignature(@RequestBody NewsSignatureDto dto) {
        News newsSignature = new News();
        newsSignature.setId(dto.getId());
        newsSignature.setTitle(dto.getTitle());
        newsSignature.setPostTime(new Date().toInstant().atOffset(ZoneOffset.UTC));
        newsSignature = newsService.saveNews(newsSignature);

        dto.setId(newsSignature.getId());
        dto.setPostTime(newsSignature.getPostTime());

        return dto;
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void saveNewsContent(@RequestBody List<NewsContentBlockDto> blocks, @PathVariable Integer id) {
        News news = newsService.findNewsByIdNoFetch(id);
        List<NewsContentBlock> result = new LinkedList<>();

        blocks.forEach( b -> {
            NewsContentBlock block = new NewsContentBlock();
            block.setNews(news);
            block.setText(b.getText());
            block.getNewsContentBlockId().setNumber(b.getBlockNumber());

            result.add(block);
        });

        result.forEach( b -> newsContentService.saveNewsContentBlock(b) );
    }

    @PostMapping("/{id}/tag")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void associateWithTag(@RequestBody TagAssociationRequestDto dto, @PathVariable Integer id) {
        if(dto.getBelongs())
            newsService.associateWithTag(id, dto.getTag());
        else
            newsService.deassociateWithTag(id, dto.getTag());
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNews(@RequestBody IdDto idDto) {
        newsService.deleteNews(idDto.getId());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNewsContent(@PathVariable Integer id) {
        News news = newsService.findNewsByIdNoFetch(id);

        newsContentService.deleteAllContentOfNews(news);
    }

}
