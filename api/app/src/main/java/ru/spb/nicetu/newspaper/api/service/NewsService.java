package ru.spb.nicetu.newspaper.api.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.spb.nicetu.newspaper.api.model.Comment;
import ru.spb.nicetu.newspaper.api.model.News;
import ru.spb.nicetu.newspaper.api.model.NewsContentBlock;
import ru.spb.nicetu.newspaper.api.model.Tag;
import ru.spb.nicetu.newspaper.api.repository.CommentRepository;
import ru.spb.nicetu.newspaper.api.repository.NewsContentRepository;
import ru.spb.nicetu.newspaper.api.repository.NewsRepository;
import ru.spb.nicetu.newspaper.api.repository.TagRepository;
import ru.spb.nicetu.newspaper.api.service.auxiliary.NewsServiceFindNewsByIdResult;
import ru.spb.nicetu.newspaper.api.service.exception.MissingEntityException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;

import javax.transaction.Transactional;

/**
 * <p>Service which encapsulates business logic for news.</p>
 *
 * <p>Can be used to perform CRUD operations on {@code News}s as well as associate {@code News} with {@code Tag}s.</p>
 * <p>Multiple news are returned by pages, size of page can be specified through application properties.</p>
 * <p>Logs its work and unpredicted exceptions.</p>
    * @author Nikita Osiptsov
    * @see {@link News}
    * @see {@link Tag}
    * @see {@link Comment}
    * @see {@link NewsRepository}
    * @see {@link TagRepository}
 * @since 1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
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
    @Value("${app.config.newsPageSize}")
    @Setter
    private Integer newsPageSize;

    public Page<News> findAllNews(Integer page) {
        log.info("Got request for all news");

        try {
            Page<News> news = newsRepository.findAllByOrderByPostTimeDesc(PageRequest.of(page, newsPageSize));

            log.info("Successfully got " + news.getNumberOfElements() + " news");

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

    public News findNewsByIdNoFetch(Long id) {
        log.info("Got request for news with id = " + id);

        try {
            Optional<News> res = newsRepository.findById(id);

            if(!res.isPresent()) {
                log.info("No news with requested id present");
                return null;
            }
            log.info("Request for " + id +  ": successfully got news (title, picturepath, tags)");

            return res.get();
        } catch (Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
           
    }

    public NewsServiceFindNewsByIdResult findNewsById(Long id) {
        try {
            News news = findNewsByIdNoFetch(id);
            if(news == null) {
                return null;
            }

            Page<Comment> comments = commentRepository
                .findAllByNewsOrderByPostTimeDesc(news, PageRequest.of(0, commentPageSize));
            news.setComments(comments.toList());
            Boolean isLastCommentsPage = comments.isLast();

            log.info("Request for " + id +  ": successfully fetched first 3 or less comments");

            Page<NewsContentBlock> contentBlocks = newsContentRepository
                .findByNewsId(id, PageRequest.of(0, textBlockPageSize));
            news.setContentBlocks(contentBlocks.toList());
            Boolean isLastContentPage = contentBlocks.isLast();

            log.info("Request for " + id +  ": successfully fetched first 5 or less blocks");

            log.info("Request for " + id +  ": success");

            return new NewsServiceFindNewsByIdResult(news, isLastContentPage, isLastCommentsPage);
        } catch (Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }

    public void associateWithTag(Long newsId, String tagName) {
        log.info("Got request to associate news with id = " + newsId + " with tag named '" + tagName + "'");

        changeAssociation(newsId, tagName, true);

        log.info("Associated news with id = " + newsId + " and tag with name = '" + tagName + "'");
    }

    public void deassociateWithTag(Long newsId, String tagName) {
        log.info("Got request to deassociate news with id = " + newsId + " with tag named '" + tagName + "'");

        changeAssociation(newsId, tagName, false);

        log.info("Deassociated news with id = " + newsId + " and tag with name = '" + tagName + "'");
    }

    public Long countLikes(Long newsId) {
        log.info("Got request to count likes of news with id = " + newsId);

        try {
            Long likesCount = newsRepository.countLikes(newsId);

            log.info("Counted " + likesCount + " likes of news with id = " + newsId);

            return likesCount;
        } catch(Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }

    public void deleteNews(Long newsId) {
        log.info("Got request to delete news with id = " + newsId);

        try {
            if(!newsRepository.existsById(newsId)) {
                throw new MissingEntityException();
            }

            newsRepository.deleteById(newsId);

            log.info("Successfully deleted news, id = " + newsId);
        } catch(MissingEntityException e) {
            log.info("Unsuccessful delete: entity does not exist");
            throw e;
        } catch(Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }

    public void deleteNews(News news) {
        deleteNews(news.getId());
    }

    private void changeAssociation(Long newsId, String tagName, boolean associate) {
        try {
           Optional<Tag> tagResult = tagRepository.findByNameFetchNews(tagName);
            if(!tagResult.isPresent()) {
                throw new MissingEntityException("Tag with name = '" + tagName + "' is not present");
            }
            Tag tag = tagResult.get();
            if(tag.getNews() == null) {
                tag.setNews(new HashSet<News>());
            }

            Optional<News> newsResult = newsRepository.findById(newsId);
            if(!newsResult.isPresent()) {
                throw new MissingEntityException("News with id = " + newsId + " not present");
            }
            News news = newsResult.get();

            if(associate && !tag.getNews().contains(news)) {
                tag.getNews().add(news);
                tagRepository.save(tag);
                return;
            }
            if(!associate && tag.getNews().contains(news)) {
                tag.getNews().remove(news);
                tagRepository.save(tag);
                return;
            }

        } catch (MissingEntityException e) {
            throw e;
        } catch (Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }
}
