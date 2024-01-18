package ru.osiptsoff.newspaper.api.environment;

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
import ru.osiptsoff.newspaper.api.model.News;
import ru.osiptsoff.newspaper.api.model.image.FileSystemImage;
import ru.osiptsoff.newspaper.api.repository.FileSystemImageRepository;
import ru.osiptsoff.newspaper.api.repository.NewsRepository;
import ru.osiptsoff.newspaper.api.service.ImageService;

@Component
@RequiredArgsConstructor
@Getter
public class FileSystemImageServiceTestEnvironment {
    @Qualifier("FileSustemImageService")
    private final ImageService<FileSystemImage> imageService;

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

        testFile = new MockMultipartFile(
                "Test image", 
                "Test image", 
                contentType.toString(), 
                "Test image".getBytes()
        );
    }

    @PreDestroy
    @Transactional
    public void destroy() {
        newsRepository.delete(testNews);
    }
}
