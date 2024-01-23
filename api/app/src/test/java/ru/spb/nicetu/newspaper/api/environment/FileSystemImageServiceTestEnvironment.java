package ru.spb.nicetu.newspaper.api.environment;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.spb.nicetu.newspaper.api.model.News;
import ru.spb.nicetu.newspaper.api.model.image.FileSystemImage;
import ru.spb.nicetu.newspaper.api.repository.FileSystemImageRepository;
import ru.spb.nicetu.newspaper.api.repository.NewsRepository;
import ru.spb.nicetu.newspaper.api.service.AbstractImageService;
import ru.spb.nicetu.newspaper.api.test.FileSystemImageServiceTests;

/**
 * <p>Environment used in {@code FileSystemImageServiceTests}.</p>
 *
 * <p>Contains tested service, repositories to load related resources, testing entities.</p>
    * @author Nikita Osiptsov
    * @see {@link FileSystemImageServiceTests}
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
@Getter
public class FileSystemImageServiceTestEnvironment {
    @Qualifier("FileSustemImageService")
    private final AbstractImageService<FileSystemImage> imageService;

    private final NewsRepository newsRepository;
    private final FileSystemImageRepository imageRepository;

    private News testNews;
    private MultipartFile testFile;
    private MediaType contentType;

    @PostConstruct
    @Transactional
    public void initialize() {
        testNews = new News();
        testNews.setTitle("Test news to test image service");
        testNews = newsRepository.save(testNews);

        contentType = MediaType.IMAGE_JPEG;

        testFile = new MockMultipartFile("Test image", 
            "Test image", 
            contentType.toString(), 
            "Test image".getBytes());
    }

    @PreDestroy
    @Transactional
    public void destroy() {
        newsRepository.delete(testNews);
    }
}
