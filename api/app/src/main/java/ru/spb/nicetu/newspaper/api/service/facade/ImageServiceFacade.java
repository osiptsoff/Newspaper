package ru.spb.nicetu.newspaper.api.service.facade;

import org.springframework.web.multipart.MultipartFile;

import ru.spb.nicetu.newspaper.api.model.image.AbstractImage;
import ru.spb.nicetu.newspaper.api.service.AbstractImageService;

/**
 * <p>Class which wraps {@link AbstractImageService} in order to provide
 * simplier interface for interaction.</p>
 * 
 * <p>Minimizes client dependencies; converts DTOs to domain objects, converts result domain objects to DTOs</p>
    * @author Nikita Osiptsov
    * @see {@link AbstractImageService}
 * @since 1.2
 */
public interface ImageServiceFacade<T extends AbstractImage> {
    AbstractImage getImage(Long id);
    void postImage(MultipartFile file, String type, Long id);
    void deleteImage( Long id);
}
