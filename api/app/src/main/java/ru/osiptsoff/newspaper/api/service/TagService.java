package ru.osiptsoff.newspaper.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.osiptsoff.newspaper.api.model.Tag;
import ru.osiptsoff.newspaper.api.repository.TagRepository;

import java.util.Optional;

import javax.transaction.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TagService {
    private final TagRepository tagRepository;

    public Tag saveTag(Tag tag) {
        log.info("Got request to save tag");

        try {
            Tag result = tagRepository.save(tag);

            log.info("Successfully saved tag, id = " + result.getId());

            return result;
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
            tagRepository.deleteById(id);

            log.info("Successfully deleted tag, id = " + id);
        } catch(Exception e) {
            log.error("Got exception: ", e);
            throw e;
        }
    }

    public void deleteTag(Tag tag) {
        deleteTag(tag.getId());
    }
}
