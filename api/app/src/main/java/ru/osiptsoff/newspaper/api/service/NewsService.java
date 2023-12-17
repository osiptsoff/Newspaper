package ru.osiptsoff.newspaper.api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.osiptsoff.newspaper.api.model.Comment;
import ru.osiptsoff.newspaper.api.model.News;
import ru.osiptsoff.newspaper.api.model.NewsContentBlock;
import ru.osiptsoff.newspaper.api.repository.CommentRepository;
import ru.osiptsoff.newspaper.api.repository.NewsContentRepository;
import ru.osiptsoff.newspaper.api.repository.NewsRepository;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class NewsService {
    private final NewsRepository newsRepository;
    private final CommentRepository commentRepository;
    private final NewsContentRepository newsContentRepository;

    @Autowired
    public NewsService(
            NewsRepository newsRepository,
            CommentRepository commentRepository,
            NewsContentRepository newsContentRepository) {
        this.newsRepository = newsRepository;
        this.commentRepository = commentRepository;
        this.newsContentRepository = newsContentRepository;
    }

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

    public News findNewsById(Integer id) {
        log.info("Got request for news with id = " + id);

        try {
            Optional<News> res = newsRepository.findById(id);

            if( !res.isPresent() ) {
                log.info("No news with requested id present");
                return null;
            }
            log.info("Request for " + id +  ": successfully got news (title, picturepath, tags)");

            News news = res.get();

            List<Comment> comments = commentRepository.findAllByNewsOrderByPostTimeDesc(news, PageRequest.of(0, 3));
            news.setComments(comments);
            log.info("Request for " + id +  ": successfully fetched first 3 or less comments");

            List<NewsContentBlock> contentBlocks = newsContentRepository.findByNewsId(id, PageRequest.of(0, 5));
            news.setContent(contentBlocks);
            log.info("Request for " + id +  ": successfully fetched first 5 or less blocks");
            log.info("Request for " + id +  ": success");

            return news;
        } catch (Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }

    public void deleteNews(News news) {
        newsRepository.delete(news);
    }
}
