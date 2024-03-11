package ru.spb.nicetu.newspaper.api.service;

import org.springframework.data.domain.Page;

import ru.spb.nicetu.newspaper.api.model.News;
import ru.spb.nicetu.newspaper.api.model.Tag;
import ru.spb.nicetu.newspaper.api.model.User;
import ru.spb.nicetu.newspaper.api.model.UserTag;

/**
 * <p>Service which encapsulates business logic for users.</p>
 *
 * <p>Can be used to perform CRUD operations on {@code User}s; 
 * to associate {@code User} with {@code Tag}s and {@code News} and check those assotiations;
 * to get news considering user's preferences.</p>
    * @author Nikita Osiptsov
    * @see {@link User}
    * @see {@link News}
    * @see {@link Tag}
    * @see {@link UserTag}
 * @since 1.0
 */
public interface UserService {
    User findByLogin(String login);
    Page<News> findPreferredNewsByLogin(String login, Integer pageNumber);
    User findByLoginFetchTags(String login);
    User saveUser(User user);
    void deleteUser(Long userId);
    void deleteUser(User user);
    Boolean isNewsLiked(String login, Long newsid);
    Boolean isTagLiked(String login, String tag);
    void likeNews(String login, Long newsId);
    void undoLikeNews(String login, Long newsId);
    void likeTag(String login, String tag);
    void dislikeTag(String login, String tag);
    void undoTagAssociation(String login, String tag);
}
