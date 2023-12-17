package ru.osiptsoff.newspaper.api.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.osiptsoff.newspaper.api.model.Comment;
import ru.osiptsoff.newspaper.api.model.News;
import ru.osiptsoff.newspaper.api.model.NewsContentBlock;
import ru.osiptsoff.newspaper.api.repository.CommentRepository;
import ru.osiptsoff.newspaper.api.repository.NewsContentRepository;
import ru.osiptsoff.newspaper.api.repository.NewsRepository;
import ru.osiptsoff.newspaper.api.service.auxiliary.NewsServiceFindNewsByIdResponse;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class NewsService {
    private final NewsRepository newsRepository;
    private final CommentRepository commentRepository;
    private final NewsContentRepository newsContentRepository;

    @Value("${app.config.commentPageSize}")
    @Setter
    private Integer commentPageSize;
    @Value("${app.config.textBlockPageSize}")
    @Setter
    private Integer textBlockPageSize;

    public List<News> findAllNews() {
        log.info("Got request for all news");

        try {
            List<News> news = newsRepository.findAllByOrderByPostTimeDesc();

            log.info("Successfully got " + news.size() + " news");

            return news;
        } catch(Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }

    }

    public News saveNews(News news) {
        log.info("Got request to save news");

        try {
            News result = newsRepository.save(news);

            log.info("Successfully saved news, id = " + result.getId());

            return result;
        } catch(Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }

    public NewsServiceFindNewsByIdResponse findNewsById(Integer id) {
        log.info("Got request for news with id = " + id);

        try {
            Optional<News> res = newsRepository.findById(id);

            if( !res.isPresent() ) {
                log.info("No news with requested id present");
                return null;
            }
            log.info("Request for " + id +  ": successfully got news (title, picturepath, tags)");

            News news = res.get();

            List<Comment> comments = commentRepository
                    .findAllByNewsOrderByPostTimeDesc(news, PageRequest.of(0, commentPageSize));
            news.setComments(comments);
            Boolean isLastCommentsPage = commentRepository.countAllByNews(news) <= commentPageSize;

            log.info("Request for " + id +  ": successfully fetched first 3 or less comments");

            List<NewsContentBlock> contentBlocks = newsContentRepository
                    .findByNewsId(id, PageRequest.of(0, textBlockPageSize));
            news.setContent(contentBlocks);
            Boolean isLastContentPage = newsContentRepository.countAllByNews(news) <= textBlockPageSize;

            log.info("Request for " + id +  ": successfully fetched first 5 or less blocks");

            log.info("Request for " + id +  ": success");

            return new NewsServiceFindNewsByIdResponse(news, isLastContentPage, isLastCommentsPage);
        } catch (Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }

    public void deleteNews(News news) {
        log.info("Got request to delete news with id = " + news.getId());

        try {
            newsRepository.delete(news);

            log.info("Successfully deleted news, id = " + news.getId());
        } catch(Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }

    }
}
