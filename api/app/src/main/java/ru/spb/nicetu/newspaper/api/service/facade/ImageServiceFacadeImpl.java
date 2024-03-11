package ru.spb.nicetu.newspaper.api.service.facade;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import ru.spb.nicetu.newspaper.api.model.image.AbstractImage;
import ru.spb.nicetu.newspaper.api.model.image.FileSystemImage;
import ru.spb.nicetu.newspaper.api.service.AbstractImageService;

/**
 * <p>{@link ImageServiceFacade} implementation for {@link FileSystemImage}</p>
 * 
    * @author Nikita Osiptsov
    * @see {@link ImageServiceFacade}
    * @see {@link FileSystemImage}
 * @since 1.1
 */
@Component
@RequiredArgsConstructor
public class ImageServiceFacadeImpl implements ImageServiceFacade<FileSystemImage> {
    private final AbstractImageService<FileSystemImage> imageService;

    @Override
    public AbstractImage getImage(Long id) {
        AbstractImage image = imageService.findImage(id);
        return image;
    }

    @Override
    public void postImage(MultipartFile file, String type, Long id) {
        MediaType mediaType = imageService.resolveMediaType(type);

        imageService.saveImage(file, mediaType, id);
    }

    @Override
    public void deleteImage(Long id) {
        imageService.deleteImage(id);
    }
    
}
