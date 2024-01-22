package ru.spb.nicetu.newspaper.api.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import ru.spb.nicetu.newspaper.api.model.image.AbstractImage;

@NoRepositoryBean
public interface ImageRepository<T extends AbstractImage> extends CrudRepository<T, Long> {
    T findByNewsId(Long newsId);
}
