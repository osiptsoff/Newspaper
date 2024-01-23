package ru.spb.nicetu.newspaper.api.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import ru.spb.nicetu.newspaper.api.model.image.FileSystemImage;
import ru.spb.nicetu.newspaper.api.model.image.AbstractImage;
import ru.spb.nicetu.newspaper.api.repository.FileSystemImageRepository;
import ru.spb.nicetu.newspaper.api.repository.NewsRepository;
import ru.spb.nicetu.newspaper.api.service.exception.ImageStorageException;

/**
 * <p>Service which encapsulates business logic for pictures stored in file system.</p>
 *
 * <p>Can be used to perform CRUD operations on {@code FileSystemImage}s.</p>
 * <p>Logs its work and unpredicted exceptions.</p>
    * @author Nikita Osiptsov
    * @see {@link AbstractImageService}
    * @see {@link FileSystemImage}
 * @since 1.0
 */
@Service
@Slf4j
public class FileSystemImageService extends AbstractImageService<FileSystemImage> {
    private Path directoryPath;

    @Autowired
    public FileSystemImageService(FileSystemImageRepository imageRepository, NewsRepository newsRepository) {
        super(imageRepository, newsRepository);
    }

    @Value("${app.config.uploadDirectory}")
    public void setDirectoryPath(String path) {
        directoryPath = Paths.get(path);
    }

    @PostConstruct
    public void initializeDirectory() throws IOException {
        try {
            Files.createDirectory(directoryPath);
        } catch (FileAlreadyExistsException e) {
        } catch (Exception e) {
            log.error("Failed to create directory for images, got exception: ", e);
            throw e;
        }
        
    }

    @Override
    public void deleteImage(Long newsId) {
        AbstractImage image = imageRepository.findByNewsId(newsId);

        super.deleteImage(newsId);

        try {
            Files.deleteIfExists(image.asResource().getFile().toPath());
        } catch (IOException e) {
            log.error("Got exception: ", e);
            throw new RuntimeException("Failed to delete file");
        } catch (Exception e) {
            log.error("Got exception", e);
            throw e;
        }
    }

    @Override
    @Transactional
    protected FileSystemImage performSave(MultipartFile file, MediaType mediaType, Long newsId) {
        try {
            if (file.isEmpty()) {
                log.info("Got empty image to save, aborted");

                throw new ImageStorageException("Empty file passed");
            }

            Path destination = this.directoryPath.resolve(Paths.get("image" + newsId));
            InputStream inputStream = file.getInputStream();
            Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);

            FileSystemImage image = new FileSystemImage();
            image.setType(mediaType.toString());
            image.setPath(destination.toString());
            image.setNewsId(newsId);
            image = imageRepository.save(image);

            return image;
        } catch (ImageStorageException e) {
            throw e;
        } catch (IOException e) {
            log.error("Got exception: ", e);
            throw new RuntimeException("Failed to save file");
        } catch (Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }
}
