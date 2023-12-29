package ru.osiptsoff.newspaper.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.osiptsoff.newspaper.api.model.News;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {
     List<News> findAllByOrderByPostTimeDesc();

     @Query(value = "SELECT count(n.likers) FROM News n WHERE n.id = :newsId")
     Integer countLikes(@Param("newsId") Integer newsId);
}
