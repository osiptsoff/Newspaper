package ru.osiptsoff.newspaper.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.osiptsoff.newspaper.api.model.News;
import ru.osiptsoff.newspaper.api.service.auxiliary.NewsLikedTagsPair;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {
     List<News> findAllByOrderByPostTimeDesc();

     @Query(value =
          "SELECT new ru.osiptsoff.newspaper.api.service.auxiliary.NewsLikedTagsPair( n, SUM(CASE WHEN uut.likes = true THEN 1 ELSE 0 END) ) FROM User u "
          + "LEFT JOIN u.tags uut " 
          + "LEFT JOIN uut.tag ut "
          + "JOIN News n ON ( (ut IN elements(n.tags)) OR (ut IS null) ) "
          + "WHERE u.login = :login "
          + "GROUP BY n "
          + "HAVING SUM(CASE WHEN uut.likes = false THEN 1 ELSE 0 END) = 0 "
          + "ORDER BY n.postTime DESC ")
     List<NewsLikedTagsPair> findAllByUserPreferencesOrderByTimeDesc(@Param("login") String login);

     @Query(value = "SELECT size(n.likers) FROM News n WHERE n.id = :newsId")
     Integer countLikes(@Param("newsId") Integer newsId);
}
