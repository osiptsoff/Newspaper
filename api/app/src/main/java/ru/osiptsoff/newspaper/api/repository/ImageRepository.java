package ru.osiptsoff.newspaper.api.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import ru.osiptsoff.newspaper.api.model.image.Image;

@NoRepositoryBean
public interface ImageRepository<T extends Image> extends CrudRepository<T, Integer> {
    T findByNewsId(Integer newsId);
}
