package ru.spb.nicetu.newspaper.api.repository;

import org.springframework.stereotype.Repository;

import ru.spb.nicetu.newspaper.api.model.image.FileSystemImage;

/**
 * <p>{@code FileSystemImage} repository.</p>
    * @author Nikita Osiptsov
    * @see {@link FileSystemImage}
 * @since 1.0
 */
@Repository
public interface FileSystemImageRepository extends ImageRepository<FileSystemImage> {
}
