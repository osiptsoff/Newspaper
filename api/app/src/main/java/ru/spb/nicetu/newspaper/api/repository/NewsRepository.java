package ru.spb.nicetu.newspaper.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.spb.nicetu.newspaper.api.model.News;
import ru.spb.nicetu.newspaper.api.service.auxiliary.NewsLikedTagsPair;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
     Page<News> findAllByOrderByPostTimeDesc(PageRequest pageRequest);

     /*
      * The pair is returned: list of suiting news sorted by post date
      * and number of tags of news liked by user
      * for subsequent sort of list by that number;
      * this os done so because two sorts (by date and tags) must be independant from each other,
      * that was considered to be impossible by JPQL means
      */
     @Query("SELECT new ru.spb.nicetu.newspaper.api.service.auxiliary.NewsLikedTagsPair "
          + "(n, SUM(CASE WHEN ut.likes = true THEN 1 ELSE 0 END)) " 
          + "FROM User u, Tag t "
          + "LEFT JOIN u.tags ut ON ut.tag = t "
          + "JOIN News n ON ( (t IN elements(n.tags)) OR (ut IS null) ) "
          + "WHERE u.login = :login "
          + "GROUP BY n "
          + "HAVING SUM(CASE WHEN ut.likes = false THEN 1 ELSE 0 END) = 0 "
          + "ORDER BY n.postTime DESC ")
     Page<NewsLikedTagsPair> findAllByUserPreferencesOrderByTimeDesc(@Param("login") String login, PageRequest pageRequest);

     @Query("SELECT size(n.likers) FROM News n WHERE n.id = :newsId")
     Long countLikes(@Param("newsId") Long newsId);
}
