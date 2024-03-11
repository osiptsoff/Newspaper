package ru.spb.nicetu.newspaper.api.service;

import java.util.List;

import ru.spb.nicetu.newspaper.api.model.Tag;

/**
 * <p>Service which encapsulates business logic for tags.</p>
 *
 * <p>Can be used to save, delete and get {@code Tag}s.</p>
    * @author Nikita Osiptsov
    * @see {@link Tag}
 * @since 1.0
 */
public interface TagService {
    List<Tag> findAllTags();
    Tag saveTag(Tag tag);
    Tag findTagByName(String name);
    void deleteTag(Long id);
    void deleteTag(Tag tag);
}
