package ru.spb.nicetu.newspaper.api.service.facade;

import java.time.ZoneOffset;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ru.spb.nicetu.newspaper.api.dto.CommentDto;
import ru.spb.nicetu.newspaper.api.dto.FetchedNewsDto;
import ru.spb.nicetu.newspaper.api.dto.NewsContentBlockDto;
import ru.spb.nicetu.newspaper.api.dto.NewsSignatureDto;
import ru.spb.nicetu.newspaper.api.dto.PageDto;
import ru.spb.nicetu.newspaper.api.dto.TagAssociationRequestDto;
import ru.spb.nicetu.newspaper.api.model.News;
import ru.spb.nicetu.newspaper.api.model.NewsContentBlock;
import ru.spb.nicetu.newspaper.api.model.embeddable.NewsContentBlockId;
import ru.spb.nicetu.newspaper.api.service.NewsContentService;
import ru.spb.nicetu.newspaper.api.service.NewsService;
import ru.spb.nicetu.newspaper.api.service.auxiliary.NewsServiceFindNewsByIdResult;

/**
 * <p>{@link NewsServiceFacade} implementation.</p>
 * 
    * @author Nikita Osiptsov
    * @see {@link NewsServiceFacade}
 * @since 1.1
 */
@Component
@RequiredArgsConstructor
public class NewsServiceFacadeImpl implements NewsServiceFacade {
    private final NewsService newsService;
    private final NewsContentService newsContentService;

    @Override
    public PageDto<NewsSignatureDto> getAllNewsPage(Integer pageNumber) {
        Page<News> page = newsService.findAllNews(pageNumber);

        List<NewsSignatureDto> resultList = page.map(n -> NewsSignatureDto.from(n)).toList();

        return new PageDto<NewsSignatureDto>(resultList, page.isLast());
    }

    @Override
    public FetchedNewsDto getNews(Long id) {
        NewsServiceFindNewsByIdResult fetchedNews = newsService.findNewsById(id);
        if(fetchedNews == null) {
            throw new NullPointerException();
        }

        FetchedNewsDto result = new FetchedNewsDto();
        result.setNewsSignature(NewsSignatureDto.from(fetchedNews.getNews()));
        result.setComments(fetchedNews
            .getNews()
            .getComments()
            .stream()
            .map(c -> CommentDto.from(c) )
            .collect(Collectors.toList()));
        result.setContent(fetchedNews
            .getNews()
            .getContentBlocks()
            .stream()
            .map(b -> NewsContentBlockDto.from(b) )
            .collect(Collectors.toList()));

        result.setLikesCount(newsService.countLikes(id));

        result.setIsLastCommentsPage(fetchedNews.getIsLastCommentsPage());
        result.setIsLastContentPage(fetchedNews.getIsLastContentPage());

        return result;
    }

    @Override
    public PageDto<NewsContentBlockDto> getNewsContentPage(Long newsId, Integer pageNumber) {
        List<NewsContentBlockDto> data = new LinkedList<>();
        Page<NewsContentBlock> page = newsContentService.findNthPageOfContent(newsId, pageNumber);

        page.forEach(b -> data.add(NewsContentBlockDto.from(b)));

        return new PageDto<NewsContentBlockDto>(data, page.isLast());
    }

    @Override
    public NewsSignatureDto saveNewsSignature(NewsSignatureDto dto) {
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

    @Override
    public void saveNewsContent(List<NewsContentBlockDto> blocks, Long id) {
        News news = newsService.findNewsByIdNoFetch(id);
        if(news == null) {
            throw new NullPointerException();
        }
        List<NewsContentBlock> result = new LinkedList<>();

        blocks.forEach(b -> {
            NewsContentBlock block = new NewsContentBlock();
            block.setNewsContentBlockId(new NewsContentBlockId());
            block.getNewsContentBlockId().setNewsId(news.getId());
            block.getNewsContentBlockId().setNumber(b.getBlockNumber());
            block.setNews(news);
            block.setText(b.getText());

            result.add(block);
        });

        newsContentService.saveMultipleNewsContentBlocks(result);
    }

    @Override
    public void associateWithTag(TagAssociationRequestDto dto, Long id) {
        if(dto.getBelongs()) {
            newsService.associateWithTag(id, dto.getTag());
        } else {
            newsService.deassociateWithTag(id, dto.getTag());
        }
    }

    @Override
    public void deleteNews(Long newsId) {
        newsService.deleteNews(newsId);
    }

    @Override
    public void deleteNewsContent(Long id) {
        News news = newsService.findNewsByIdNoFetch(id);

        newsContentService.deleteAllContentOfNews(news);
    }
    
}
