package ru.spb.nicetu.newspaper.api.test;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import ru.spb.nicetu.newspaper.api.model.Tag;
import ru.spb.nicetu.newspaper.api.repository.TagRepository;
import ru.spb.nicetu.newspaper.api.service.TagService;

/**
 * <p>Tests for {@code TagService} features.</p>
 *
 * <p>Contains tests for all methods of {@code TagService}.</p>
    * @author Nikita Osiptsov
    * @see {@link TagService}
 * @since 1.0
 */
@SpringBootTest
@Transactional
public class TagServiceTests {
    private final TagRepository tagRepository;
    private final TagService tagService;

    @Autowired
    public TagServiceTests(TagRepository tagRepository, TagService tagService) {
        this.tagRepository = tagRepository;
        this.tagService = tagService;
    }

    @Test
    public void findAllTest() {
        Tag tag1 = new Tag();
        Tag tag2 = new Tag();

        tag1.setName("find all tags tag1");
        tag1 = tagRepository.save(tag1);
        tag2.setName("find all tags tag2");
        tag2 = tagRepository.save(tag2);

        List<Tag> tags = tagService.findAllTags();

        Assert.isTrue(tags.size() > 2, "At least 2 tags must be present");
        Assert.isTrue(tags.contains(tag1), "First tag must be present");
        Assert.isTrue(tags.contains(tag2), "Second tag must be present");

        tagRepository.delete(tag1);
        tagRepository.delete(tag2);
    }

    @Test
    public void saveTagTest() {
        Tag tag = new Tag();
        tag.setName("Test tag");

        tag = tagService.saveTag(tag);

        Optional<Tag> dbTag = tagRepository.findById(tag.getId());

        Assert.isTrue(dbTag.isPresent(), "Must be present");

        tagRepository.delete(tag);
    }
    
    @Test
    public void findByNameTest() {
        Tag tag = new Tag();
        tag.setName("Test tag");

        tag = tagService.saveTag(tag);

        Tag dbTag = tagService.findTagByName(tag.getName());

        Assert.notNull(dbTag, "Must be present");
        Assert.isTrue(dbTag.equals(tag), "Must be equal to one it's created from");

        tagRepository.delete(tag);
    }

    @Test
    public void deleteTagTest() {
        Tag tag = new Tag();
        tag.setName("Test tag");

        tag = tagService.saveTag(tag);

        Assert.isTrue(tagRepository.existsById(tag.getId()), "Must exist in db");

        tagService.deleteTag(tag);

        Assert.isTrue(!tagRepository.existsById(tag.getId()), "Must not exist in db");

    }
}
