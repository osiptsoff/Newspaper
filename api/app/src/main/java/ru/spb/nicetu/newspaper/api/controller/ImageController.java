package ru.spb.nicetu.newspaper.api.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import ru.spb.nicetu.newspaper.api.model.image.AbstractImage;
import ru.spb.nicetu.newspaper.api.model.image.FileSystemImage;
import ru.spb.nicetu.newspaper.api.service.facade.ImageServiceFacade;

/**
 * <p>Controller for '/news/{id}/image' endpoint.</p>
 *
 * <p>Provides API for posting, updating, deletion and reading picture associated with news with given id.</p>
    * @author Nikita Osiptsov
    * @see {@link ImageServiceFacade}
 * @since 1.0
 */
@RestController
@RequestMapping("news/{id}/image")
@RequiredArgsConstructor
@Validated
public class ImageController {
    private final ImageServiceFacade<FileSystemImage> imageServiceFacade;

    @GetMapping()
    public ResponseEntity<Resource> getImage(@PathVariable("id") Long id) {
        AbstractImage image = imageServiceFacade.getImage(id);

        return ResponseEntity
            .ok()
            .contentType(MediaType.valueOf(image.getType()))
            .body(image.asResource());
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void postImage(@RequestParam("file") MultipartFile file,
            @RequestHeader("Image-Type") String type,
            @PathVariable("id") Long id) {
        imageServiceFacade.postImage(file, type, id);
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteImage(@PathVariable("id") Long id) {
        imageServiceFacade.deleteImage(id);
    }
}
