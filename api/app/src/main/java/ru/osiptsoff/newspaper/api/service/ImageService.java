package ru.osiptsoff.newspaper.api.service;

import javax.transaction.Transactional;

import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.osiptsoff.newspaper.api.model.image.Image;
import ru.osiptsoff.newspaper.api.repository.ImageRepository;
import ru.osiptsoff.newspaper.api.repository.NewsRepository;
import ru.osiptsoff.newspaper.api.service.exception.ImageStorageException;
import ru.osiptsoff.newspaper.api.service.exception.MissingEntityException;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public abstract class ImageService<T extends Image> {
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
        
        if(
            mediaType.isCompatibleWith(MediaType.IMAGE_JPEG)    ||
            mediaType.isCompatibleWith(MediaType.IMAGE_PNG)     ||
            mediaType.isCompatibleWith(MediaType.IMAGE_GIF)
        ) return mediaType;

        log.info("Unsupported media type: " + type);
        throw new ImageStorageException("Unsupported media type");
    }

    public T findImage(Integer newsid) {
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

    public void deleteImage(Integer newsId) {
        log.info("Got request to delete image of news with id = " + newsId);

        try {
            if(!imageRepository.existsById(newsId))
                throw new MissingEntityException();

            imageRepository.deleteById(newsId);

            log.info("Successfully deleted image of news with id = " + newsId);
        } catch(MissingEntityException e) {
            log.info("Unsuccessful delete: entity does not exist");
            throw e;
        } catch(Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }

    public T saveImage(MultipartFile image, MediaType mediaType, Integer newsId) {
        log.info("Got request to save image of news with id = " + newsId);

        if(!newsRepository.existsById(newsId)) {
            log.info("News with id = " + newsId + " does not exist");
            throw new MissingEntityException("News does not exist");
        }

        log.info("Successfully saved image of news with id = " + newsId);

        return performSave(image, mediaType, newsId);
    }

    protected abstract T performSave(MultipartFile image, MediaType mediaType, Integer newsId);
}
