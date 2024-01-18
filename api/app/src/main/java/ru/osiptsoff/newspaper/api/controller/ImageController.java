package ru.osiptsoff.newspaper.api.controller;

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
import ru.osiptsoff.newspaper.api.model.image.FileSystemImage;
import ru.osiptsoff.newspaper.api.model.image.Image;
import ru.osiptsoff.newspaper.api.service.ImageService;

@RestController
@RequestMapping("news/{id}/image")
@RequiredArgsConstructor
@Validated
public class ImageController {
    private final ImageService<FileSystemImage> imageService;

    @GetMapping()
    public ResponseEntity<Resource> getImage(@PathVariable("id") Integer id) {
        Image image = imageService.findImage(id);
        return ResponseEntity
            .ok()
            .contentType(MediaType.valueOf(image.getType()))
            .body(image.asResource());
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void postImage(
        @RequestParam("file") MultipartFile file,
        @RequestHeader("Image-Type") String type,
        @PathVariable("id") Integer id
    ) {
        MediaType mediaType = imageService.resolveMediaType(type);

        imageService.saveImage(file, mediaType, id);
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteImage(@PathVariable("id") Integer id) {
        imageService.deleteImage(id);
    }
}
