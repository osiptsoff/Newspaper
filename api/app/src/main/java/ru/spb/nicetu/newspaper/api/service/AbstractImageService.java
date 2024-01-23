package ru.spb.nicetu.newspaper.api.service;

import javax.transaction.Transactional;

import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.spb.nicetu.newspaper.api.model.image.AbstractImage;
import ru.spb.nicetu.newspaper.api.repository.ImageRepository;
import ru.spb.nicetu.newspaper.api.repository.NewsRepository;
import ru.spb.nicetu.newspaper.api.service.exception.ImageStorageException;
import ru.spb.nicetu.newspaper.api.service.exception.MissingEntityException;

/**
 * <p>Abstract service which encapsulates common logic for {@code AbstractImage}s.</p>
 *
 * <p>Implementations should use find and delete methods of this class.</p>
 * <p>Logs its work and unpredicted exceptions.</p>
    * @author Nikita Osiptsov
    * @see {@link AbstractImage}
    * @see {@link ImageRepository}
 * @since 1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public abstract class AbstractImageService<T extends AbstractImage> {
    protected final ImageRepository<T> imageRepository;
    private final NewsRepository newsRepository;

    public MediaType resolveMediaType(String type) {
        MediaType mediaType;
        try {
            mediaType = MediaType.valueOf(type);
        } catch (InvalidMediaTypeException e) {
            log.info("Invalid media type: " + type);
            throw new ImageStorageException("Invalid media type");
        }
        
        if(mediaType.isCompatibleWith(MediaType.IMAGE_JPEG)
                || mediaType.isCompatibleWith(MediaType.IMAGE_PNG)
                || mediaType.isCompatibleWith(MediaType.IMAGE_GIF)) {
            return mediaType;
        }

        log.info("Unsupported media type: " + type);
        throw new ImageStorageException("Unsupported media type");
    }

    public T findImage(Long newsid) {
        log.info("Got request for image of news with id = " + newsid);

        try {
            T image = imageRepository.findByNewsId(newsid);

            if(image == null) {
                log.info("No image of news with id = " + newsid + " is present");

                return null;
            }

            log.info("Successfully got image of news with id = " + newsid);

            return image;
        } catch(Exception e) {
            log.error("Got exception: " + e);
            throw e;
        }
    }

    public void deleteImage(Long newsId) {
        log.info("Got request to delete image of news with id = " + newsId);

        try {
            if(!imageRepository.existsById(newsId)) {
                log.info("Unsuccessful delete: entity does not exist");
                throw new MissingEntityException();
            }
                
            imageRepository.deleteById(newsId);

            log.info("Successfully deleted image of news with id = " + newsId);
        } catch(MissingEntityException e) {
            throw e;
        } catch(Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }

    public T saveImage(MultipartFile image, MediaType mediaType, Long newsId) {
        log.info("Got request to save image of news with id = " + newsId);

        if(!newsRepository.existsById(newsId)) {
            log.info("News with id = " + newsId + " does not exist");
            throw new MissingEntityException("News does not exist");
        }

        T savedImage = performSave(image, mediaType, newsId);

        log.info("Successfully saved image of news with id = " + newsId);

        return savedImage;
    }

    protected abstract T performSave(MultipartFile image, MediaType mediaType, Long newsId);
}
