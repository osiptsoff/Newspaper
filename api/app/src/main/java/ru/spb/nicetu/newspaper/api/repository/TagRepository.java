package ru.spb.nicetu.newspaper.api.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.spb.nicetu.newspaper.api.model.Tag;

import java.util.List;
import java.util.Optional;

/**
 * <p>{@code Tag} repository.</p>
    * @author Nikita Osiptsov
    * @see {@link Tag}
 * @since 1.0
 */
@Repository
public interface TagRepository extends CrudRepository<Tag, Long> {
    List<Tag> findAll();
    Optional<Tag> findByName(String name);
    Boolean existsByName(String name);
    Long deleteByName(String name);

    @Query("SELECT t FROM Tag t LEFT JOIN FETCH t.news WHERE t.name = :name")
    Optional<Tag> findByNameFetchNews(@Param("name") String name);

}
