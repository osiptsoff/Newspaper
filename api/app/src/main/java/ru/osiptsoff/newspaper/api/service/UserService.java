package ru.osiptsoff.newspaper.api.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.osiptsoff.newspaper.api.model.News;
import ru.osiptsoff.newspaper.api.model.Tag;
import ru.osiptsoff.newspaper.api.model.User;
import ru.osiptsoff.newspaper.api.model.UserTag;
import ru.osiptsoff.newspaper.api.model.auth.UserPrincipal;
import ru.osiptsoff.newspaper.api.model.embeddable.UserTagId;
import ru.osiptsoff.newspaper.api.repository.NewsRepository;
import ru.osiptsoff.newspaper.api.repository.TagRepository;
import ru.osiptsoff.newspaper.api.repository.UserRepository;
import ru.osiptsoff.newspaper.api.repository.UserTagRepository;
import ru.osiptsoff.newspaper.api.service.exceptions.MissingEntityException;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final NewsRepository newsRepository;
    private final TagRepository tagRepository;
    private final UserTagRepository userTagRepository;

    public User findByLogin(String login) {
        log.info("Got request for user with login = " + login);

        try {
            Optional<User> res = userRepository.findByLogin(login);
            if(!res.isPresent()) {
                log.info("No user with login = '" + login + "'' present");
                return null;
            }
            User user = res.get();

            log.info("Request for " + login +  ": successfully got user, id = " + user.getId());

            return user;
        } catch(Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }

    public User saveUser(User user) {
        log.info("Got request to save user '" + user.getLogin() + "'");

        try {
            User result = userRepository.save(user);

            log.info("Successfully saved user '" + result.getLogin() + "', id = " + result.getId());

            return result;
        } catch(Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }

    public void deleteUser(Integer userId) {
        log.info("Got request to delete user with id = " + userId);

        try {
            userRepository.deleteById(userId);

            log.info("Successfully deleted user, id = " + userId);
        } catch(Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }

    public void deleteUser(User user) {
        deleteUser(user.getId());
    }

    public Boolean isNewsLiked(String login, Integer newsid) {
        log.info("Got request for user '" + login + "'s attitude for news with id = " + newsid);

        try {
            Boolean liked = userRepository.userLikesNews(login, newsid);

            log.info("'" + login + "' liked news with id =" + newsid + "': " + liked);

            return liked;
        } catch(Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }

    public Boolean isTagLiked(String login, String tag) {
        log.info("Got request for user '" + login + "'s attitude for tag '" + tag + "'");

        try {
            Boolean attitude = userRepository.userLikesTag(login, tag);

            log.info("'" + login + "' likes tag '" + tag + "': " + attitude);

            return attitude;
        } catch(Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }

    public void likeNews(String login, Integer newsId) {
        log.info("Got request to like news with id = " + newsId + " by user '" + login + "'");

        likeNews(login, newsId, true);

        log.info("Successfully liked news with id = " + newsId + " by user '" + login + "'");
    }

    public void undoLikeNews(String login, Integer newsId) {
        log.info("Got request to undo like news with id = " + newsId + " by user '" + login + "'");

        likeNews(login, newsId, false);

        log.info("Successfully undid like of news with id = " + newsId + " by user '" + login + "'");
    }

    public void likeTag(String login, String tag) {
        log.info("Got request to like tag '" + tag + "'' by user '" + login + "'");

        associateWithTag(login, tag, true);

        log.info("Successfully liked tag '" + tag + "'' by user '" + login + "'");
    }

    public void dislikeTag(String login, String tag) {
        log.info("Got request to dislike tag '" + tag + "'' by user '" + login + "'");

        associateWithTag(login, tag, false);

        log.info("Successfully liked tag '" + tag + "'' by user '" + login + "'");
    }

    public void undoTagAssociation(String login, String tag) {
        log.info("Got request to undo association with tag '" + tag + "'' and user '" + login + "'");

        associateWithTag(login, tag, null);

        log.info("Successfully undid association with tag '" + tag + "'' and user '" + login + "'");
    }

    private void likeNews(String login, Integer newsId, boolean like) {
        try {
            Optional<User> res = userRepository.findByLogin(login);
            if(!res.isPresent()) {
                throw new MissingEntityException("No user with login = '" + login + "'' present");
            }
            User user = res.get();
 
            Optional<News> newsResult = newsRepository.findById(newsId);
            if(!newsResult.isPresent()) {
                throw new MissingEntityException("News with id = " + newsId + " not present");
            }
            News news = newsResult.get();
 
             if(like)
                 user.getLikedNews().add(news);
             else
                 user.getLikedNews().remove(news);
 
             userRepository.save(user);
         } catch (Exception e) {
             log.error("Got exception: ", e);
             throw e;
         }
    }

    private void associateWithTag(String login, String tagName, Boolean association) {
        try {
            Optional<User> res = userRepository.findByLogin(login);
            if(!res.isPresent()) {
                throw new MissingEntityException("No user with login = '" + login + "'' present");
            }
            User user = res.get();
 
            Optional<Tag> tagResult = tagRepository.findByName(tagName);
            if(!tagResult.isPresent()) {
                throw new MissingEntityException("Tag with name = '" + tagName + "' is not present");
            }
            Tag tag = tagResult.get();

            UserTag userTag = new UserTag(
                new UserTagId(user.getId(), tag.getId()),
                user,
                tag,
                association
            );
 
            if(association != null) {
                userTagRepository.save(userTag);
            }
            else
                userTagRepository.delete(userTag);
 
             userRepository.save(user);
         } catch (Exception e) {
             log.error("Got exception: ", e);
             throw e;
         }
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByLogin(username);
        if(user == null) {
            throw new UsernameNotFoundException("No user with requested username");
        }

        return new UserPrincipal(user);
    }    
}
