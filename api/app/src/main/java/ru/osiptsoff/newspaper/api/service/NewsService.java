package ru.osiptsoff.newspaper.api.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.osiptsoff.newspaper.api.model.Comment;
import ru.osiptsoff.newspaper.api.model.News;
import ru.osiptsoff.newspaper.api.model.NewsContentBlock;
import ru.osiptsoff.newspaper.api.model.Tag;
import ru.osiptsoff.newspaper.api.repository.CommentRepository;
import ru.osiptsoff.newspaper.api.repository.NewsContentRepository;
import ru.osiptsoff.newspaper.api.repository.NewsRepository;
import ru.osiptsoff.newspaper.api.repository.TagRepository;
import ru.osiptsoff.newspaper.api.service.auxiliary.NewsServiceFindNewsByIdResult;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class NewsService {
    private final NewsRepository newsRepository;
    private final CommentRepository commentRepository;
    private final NewsContentRepository newsContentRepository;
    private final TagRepository tagRepository;

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

    public NewsServiceFindNewsByIdResult findNewsById(Integer id) {
        log.info("Got request for news with id = " + id);

        try {
            Optional<News> res = newsRepository.findById(id);

            if( !res.isPresent() ) {
                log.info("No news with requested id present");
                return null;
            }
            log.info("Request for " + id +  ": successfully got news (title, picturepath, tags)");

            News news = res.get();

            Page<Comment> comments = commentRepository
                    .findAllByNewsOrderByPostTimeDesc(news, PageRequest.of(0, commentPageSize));
            news.setComments(comments.toList());
            Boolean isLastCommentsPage = comments.isLast();

            log.info("Request for " + id +  ": successfully fetched first 3 or less comments");

            Page<NewsContentBlock> contentBlocks = newsContentRepository
                    .findByNewsId(id, PageRequest.of(0, textBlockPageSize));
            news.setContent(contentBlocks.toList());
            Boolean isLastContentPage = contentBlocks.isLast();

            log.info("Request for " + id +  ": successfully fetched first 5 or less blocks");

            log.info("Request for " + id +  ": success");

            return new NewsServiceFindNewsByIdResult(news, isLastContentPage, isLastCommentsPage);
        } catch (Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }

    public void associateWithTag(Integer newsId, String tagName) {
        log.info("Got request to associate news with id = " + newsId + " with tag named '" + tagName + "'");

        try {
            Optional<Tag> tagResult = tagRepository.findByName(tagName);
            if(!tagResult.isPresent()) {
                log.info("Tag with name = '" + tagName + "' is not present");
                return;
            }
            Tag tag = tagResult.get();

            if(!newsRepository.existsById(newsId)) {
                log.info("News with id = " + tagName + " not present");
                return;
            }

            tagRepository.associate(newsId, tag.getId());

            log.info("Aassociated news with id = " + newsId + " and tag with name = '" + tagName + "'");
        } catch (Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }

    public void deassociateWithTag(Integer newsId, String tagName) {
        log.info("Got request to deassociate news with id = " + newsId + " with tag named '" + tagName + "'");

        try {
            Optional<Tag> tagResult = tagRepository.findByName(tagName);
            if(!tagResult.isPresent()) {
                log.info("Tag with name = '" + tagName + "' is not present");
                return;
            }
            Tag tag = tagResult.get();

            tagRepository.deassociate(newsId, tag.getId());

            log.info("Deassociated news with id = " + newsId + " and tag with name = '" + tagName + "'");
        } catch (Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }

    public void deleteNews(Integer id) {
        log.info("Got request to delete news with id = " + id);

        try {
            newsRepository.deleteById(id);

            log.info("Successfully deleted news, id = " + id);
        } catch(Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }

    public void deleteNews(News news) {
        deleteNews(news.getId());
    }
}
