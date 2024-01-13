package ru.osiptsoff.newspaper.api.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.osiptsoff.newspaper.api.model.Tag;

import java.util.Optional;

@Repository
public interface TagRepository extends CrudRepository<Tag, Integer> {
    Optional<Tag> findByName(String name);
    Boolean existsByName(String name);
    Long deleteByName(String name);

    @Query(value = "SELECT t FROM Tag t LEFT JOIN FETCH t.news WHERE t.name = :name")
    Optional<Tag> findByNameFetchNews(@Param("name") String name);

}
