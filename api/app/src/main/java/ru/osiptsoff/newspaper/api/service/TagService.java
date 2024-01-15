package ru.osiptsoff.newspaper.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import ru.osiptsoff.newspaper.api.model.Tag;
import ru.osiptsoff.newspaper.api.repository.TagRepository;
import ru.osiptsoff.newspaper.api.service.exception.EntityExistsException;
import ru.osiptsoff.newspaper.api.service.exception.MissingEntityException;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TagService {
    private final TagRepository tagRepository;

    public List<Tag> findAllTags() {
        log.info("Got request for all tags");

        try {
            List<Tag> tags = tagRepository.findAll();

            log.info("Successfully got " + tags.size() + " tags");

            return tags;
        } catch(Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }

    public Tag saveTag(Tag tag) {
        log.info("Got request to save tag");

        try {
            if(tagRepository.existsByName(tag.getName())) {
                 log.info("Tag already exists");
                 throw new EntityExistsException();
            }
               
            Tag result = tagRepository.save(tag);

            log.info("Successfully saved tag, id = " + result.getId());

            return result;
        } catch(EntityExistsException e) {
            throw e;
        } catch(Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }

    public Tag findTagByName(String name) {
        log.info("Got request for tag '" + name + "'");

        try {
            Optional<Tag> result = tagRepository.findByName(name);

            if(!result.isPresent()) {
                log.info("No tag with name '" + name + "'");
                return null;
            }
            Tag tag = result.get();

            log.info("Successfully retrieved tag, id = " + tag.getId() + ", name = '" + tag.getName() + "'");

            return tag;
        } catch(Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }

    public void deleteTag(Integer id) {
        log.info("Got request to delete tag with id = " + id);

        try {
            if(!tagRepository.existsById(id))
                throw new MissingEntityException();

            tagRepository.deleteById(id);

            log.info("Successfully deleted tag, id = " + id);
        } catch(MissingEntityException e) {
            log.info("Unsuccessful delete: entity does not exist");
            throw e;
        } catch(Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }

    public void deleteTag(Tag tag) {
        deleteTag(tag.getId());
    }
}
