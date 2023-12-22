package ru.osiptsoff.newspaper.api.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.osiptsoff.newspaper.api.model.Tag;

import java.util.Optional;

import javax.transaction.Transactional;

@Repository
public interface TagRepository extends CrudRepository<Tag, Integer> {
    Optional<Tag> findByName(String name);
    void deleteByName(String name);

    @Transactional
    @Modifying()
    @Query(value = "DELETE FROM subject.news_tag WHERE newsid = :newsid AND tagid = :tagid", nativeQuery = true)
    void deassociate(@Param("newsid") Integer newsId, @Param("tagid") Integer tagId);

    @Transactional
    @Modifying()
    @Query(value = "INSERT INTO subject.news_tag VALUES (:newsid, :tagid)", nativeQuery = true)
    void associate(@Param("newsid") Integer newsId, @Param("tagid") Integer tagId);
}