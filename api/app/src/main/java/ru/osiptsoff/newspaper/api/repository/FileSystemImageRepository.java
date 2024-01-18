package ru.osiptsoff.newspaper.api.repository;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import ru.osiptsoff.newspaper.api.model.image.FileSystemImage;

@Repository
@Primary
public interface FileSystemImageRepository extends ImageRepository<FileSystemImage> {
}
