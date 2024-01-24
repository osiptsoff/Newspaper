package ru.spb.nicetu.newspaper.api.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.spb.nicetu.newspaper.api.model.News;
import ru.spb.nicetu.newspaper.api.model.Tag;
import ru.spb.nicetu.newspaper.api.model.User;
import ru.spb.nicetu.newspaper.api.model.UserTag;
import ru.spb.nicetu.newspaper.api.model.auth.UserPrincipal;
import ru.spb.nicetu.newspaper.api.model.embeddable.UserTagId;
import ru.spb.nicetu.newspaper.api.repository.NewsRepository;
import ru.spb.nicetu.newspaper.api.repository.TagRepository;
import ru.spb.nicetu.newspaper.api.repository.UserRepository;
import ru.spb.nicetu.newspaper.api.repository.UserTagRepository;
import ru.spb.nicetu.newspaper.api.service.auxiliary.NewsLikedTagsPair;
import ru.spb.nicetu.newspaper.api.service.exception.MissingEntityException;

/**
 * <p>Service which encapsulates business logic for users.</p>
 *
 * <p>Can be used to perform CRUD operations on {@code User}s; 
 * to associate {@code User} with {@code Tag}s and {@code News} and check those assotiations;
 * to get news considering user's preferences.</p>
 * <p>Logs its work and unpredicted exceptions.</p>
    * @author Nikita Osiptsov
    * @see {@link User}
    * @see {@link News}
    * @see {@link Tag}
    * @see {@link UserTag}
    * @see {@link UserRepository}
    * @see {@link NewsRepository}
    * @see {@link TagRepository}
    * @see {@link UserTagRepository}
 * @since 1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final NewsRepository newsRepository;
    private final TagRepository tagRepository;
    private final UserTagRepository userTagRepository;

    @Value("${app.config.newsPageSize}")
    @Setter
    private Integer newsPageSize;

    public User findByLogin(String login) {
        log.info("Got request for user with login = " + login);

        try {
            Optional<User> res = userRepository.findByLogin(login);
            if(!res.isPresent()) {
                log.info("No user with login = '" + login + "' present");
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

    public Page<News> findPreferredNewsByLogin(String login, Integer pageNumber) {
        log.info("Got request for preferred news of user with login = " + login);

        try {
            Page<NewsLikedTagsPair> result = newsRepository
                .findAllByUserPreferencesOrderByTimeDesc(login,
                    PageRequest.of(pageNumber, newsPageSize));

            List<News> data = result
                .stream()
                .sorted((i1, i2) -> i2.getLikedTags().compareTo(i1.getLikedTags()))
                .map(i -> i.getNews())
                .collect(Collectors.toList());

            log.info("Request for news for user '" + login + "': success, got " + result.getNumberOfElements() + " news");
            
            return new PageImpl<News>(data, result.getPageable(), result.getTotalElements());
        } catch(Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }

    public User findByLoginFetchTags(String login) {
        log.info("Got request for user with login = " + login + ", fetch tags");

        try {
            Optional<User> res = userRepository.findByLoginFetchTags(login);
            if(!res.isPresent()) {
                log.info("No user with login = '" + login + "' present");
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

    @Transactional
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

    @Transactional
    public void deleteUser(Long userId) {
        log.info("Got request to delete user with id = " + userId);

        try {
            if(!userRepository.existsById(userId))
                throw new MissingEntityException();

            userRepository.deleteById(userId);

            log.info("Successfully deleted user, id = " + userId);
        } catch(MissingEntityException e) {
            log.info("Unsuccessful delete: entity does not exist");
            throw e;
        } catch(Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }

    public void deleteUser(User user) {
        deleteUser(user.getId());
    }

    @Transactional
    public Boolean isNewsLiked(String login, Long newsid) {
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

    public void likeNews(String login, Long newsId) {
        log.info("Got request to like news with id = " + newsId + " by user '" + login + "'");

        likeNews(login, newsId, true);

        log.info("Successfully liked news with id = " + newsId + " by user '" + login + "'");
    }

    public void undoLikeNews(String login, Long newsId) {
        log.info("Got request to undo like news with id = " + newsId + " by user '" + login + "'");

        likeNews(login, newsId, false);

        log.info("Successfully undid like of news with id = " + newsId + " by user '" + login + "'");
    }

    public void likeTag(String login, String tag) {
        log.info("Got request to like tag '" + tag + "' by user '" + login + "'");

        associateWithTag(login, tag, true);

        log.info("Successfully liked tag '" + tag + "' by user '" + login + "'");
    }

    public void dislikeTag(String login, String tag) {
        log.info("Got request to dislike tag '" + tag + "' by user '" + login + "'");

        associateWithTag(login, tag, false);

        log.info("Successfully liked tag '" + tag + "' by user '" + login + "'");
    }

    public void undoTagAssociation(String login, String tag) {
        log.info("Got request to undo association with tag '" + tag + "' and user '" + login + "'");

        associateWithTag(login, tag, null);

        log.info("Successfully undid association with tag '" + tag + "' and user '" + login + "'");
    }

    public UserDetails userToDetails(User user) {
        return new UserPrincipal(user);
    }

    public User getAuthenticatedUser() {
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) {
            throw new BadCredentialsException("Unauthorized");
        }
            
        String login = authentication.getPrincipal().toString();

        Optional<User> res = userRepository.findByLogin(login);
        if(!res.isPresent()) {
            log.error("Tried to determine auhtenticated user; acquired incorrect username: '" + login + "'");
            throw new BadCredentialsException("Unauthorized");
        }

        return res.get();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByLogin(username);
        if(user == null) {
            throw new UsernameNotFoundException("No user with requested username");
        }

        return userToDetails(user);
    }  

    @Transactional
    private void likeNews(String login, Long newsId, boolean like) {
        try {
            Optional<User> res = userRepository.findByLoginFetchLikedNews(login);
            if(!res.isPresent()) {
                throw new MissingEntityException("No user with login = '" + login + "' present");
            }
            User user = res.get();
 
            Optional<News> newsResult = newsRepository.findById(newsId);
            if(!newsResult.isPresent()) {
                throw new MissingEntityException("News with id = " + newsId + " not present");
            }
            News news = newsResult.get();
 
            if(like && !user.getLikedNews().contains(news)) {
                user.getLikedNews().add(news);
                userRepository.save(user);
                return;
            }  
            if(!like && user.getLikedNews().contains(news)) {
                user.getLikedNews().remove(news);
                userRepository.save(user);
                return;
            }
        } catch (MissingEntityException e) {
            throw e;
        } catch (Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }

    @Transactional
    private void associateWithTag(String login, String tagName, Boolean association) {
        try {
            Optional<User> res = userRepository.findByLogin(login);
            if(!res.isPresent()) {
                log.error("Non-existent user");
                throw new MissingEntityException("No user with login = '" + login + "' present");
            }
            User user = res.get();
 
            Optional<Tag> tagResult = tagRepository.findByName(tagName);
            if(!tagResult.isPresent()) {
                log.error("Non-existent tag");
                throw new MissingEntityException("Tag with name = '" + tagName + "' is not present");
            }
            Tag tag = tagResult.get();

            UserTag userTag = new UserTag(new UserTagId(user.getId(), tag.getId()),
                user,
                tag,
                association);
 
            if(association != null) {
                userTagRepository.save(userTag);
            }
            else if(userTagRepository.existsById(userTag.getUserTagId())) {
                userTagRepository.deleteById(userTag.getUserTagId());
            }
        } catch (MissingEntityException e) {
            throw e;
        } catch (Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }  
}
