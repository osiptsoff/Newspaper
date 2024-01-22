package ru.spb.nicetu.newspaper.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.spb.nicetu.newspaper.api.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByLogin(String login);
    Boolean existsByLogin(String login);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.likedNews WHERE u.login = :login")
    Optional<User> findByLoginFetchLikedNews(@Param("login") String login);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.tags ut LEFT JOIN FETCH ut.tag WHERE u.login = :login")
    Optional<User> findByLoginFetchTags(@Param("login") String login);
    
    @Query("SELECT ut.likes FROM UserTag ut WHERE ut.tag.name = :tag AND ut.user.login = :login")
    Boolean userLikesTag(@Param("login") String login, @Param("tag") String tag);

    @Query("SELECT CASE WHEN (COUNT(n) > 0) THEN TRUE ELSE FALSE END "
        + "FROM News n " 
        + "LEFT JOIN n.likers l "
        + "WHERE n.id = :newsid AND l.login = :login ")
    Boolean userLikesNews(@Param("login") String login, @Param("newsid") Long newsid);
}
