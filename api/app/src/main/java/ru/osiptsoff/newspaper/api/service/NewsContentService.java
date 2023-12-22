package ru.osiptsoff.newspaper.api.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.osiptsoff.newspaper.api.model.News;
import ru.osiptsoff.newspaper.api.model.NewsContentBlock;
import ru.osiptsoff.newspaper.api.repository.NewsContentRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class NewsContentService {
    private final NewsContentRepository newsContentRepository;

    @Value("${app.config.textBlockPageSize}")
    @Setter
    private Integer textBlockPageSize;

    public NewsContentBlock saveNewsContentBlock(NewsContentBlock newsContentBlock) {
        log.info("Got request to save news content block");

        try {
            NewsContentBlock result = newsContentRepository.save(newsContentBlock);

            log.info("Successfully saved block, key: " + result.getNewsContentBlockId());

            return result;
        } catch(Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }

    public Page<NewsContentBlock> findNthPageOfContent(News news, Integer page) {
        log.info("Got request for news content; page = " + page + ", news id = " + news.getId());

        try {
            Page<NewsContentBlock> result = newsContentRepository
                    .findByNewsId(news.getId(), PageRequest.of(page, textBlockPageSize));

            log.info("Successfully got " + result.getSize() + " blocks");

            return result;
        } catch(Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }

    public void deleteAllContentOfNews(News news) {
        log.info("Got request to delete content of news with id = " + news.getId());

        try {
            newsContentRepository.deleteByNews(news);

            log.info("Successfully deleted content of news with id = " + news.getId());
        } catch(Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }
}
