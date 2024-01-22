package ru.spb.nicetu.newspaper.api.repository;

import org.springframework.stereotype.Repository;

import ru.spb.nicetu.newspaper.api.model.image.FileSystemImage;

@Repository
public interface FileSystemImageRepository extends ImageRepository<FileSystemImage> {
}
