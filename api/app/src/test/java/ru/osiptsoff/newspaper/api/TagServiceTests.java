package ru.osiptsoff.newspaper.api;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import ru.osiptsoff.newspaper.api.model.Tag;
import ru.osiptsoff.newspaper.api.repository.TagRepository;
import ru.osiptsoff.newspaper.api.service.TagService;

@SpringBootTest
public class TagServiceTests {
    private final TagRepository tagRepository;
    private final TagService tagService;

    @Autowired
    public TagServiceTests(TagRepository tagRepository, TagService tagService) {
        this.tagRepository = tagRepository;
        this.tagService = tagService;
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
