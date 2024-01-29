package ru.spb.nicetu.newspaper.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.spb.nicetu.newspaper.api.dto.TagDto;
import ru.spb.nicetu.newspaper.api.model.Tag;
import ru.spb.nicetu.newspaper.api.service.TagService;

/**
 * <p>Controller for '/tag' endpoint.</p>
 *
 * <p>Provides API for posting, updating, deletion and reading tags.</p>
    * @author Nikita Osiptsov
    * @see {@link TagService}
 * @since 1.0
 */
@RestController
@RequestMapping("/tag")
@ResponseStatus(HttpStatus.NO_CONTENT)
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<TagDto> findAllTags() {
        return tagService.findAllTags()
            .stream()
            .map(t -> TagDto.from(t))
            .collect(Collectors.toList());
    }

    @PostMapping()
    public void saveTag(@Valid @RequestBody TagDto tagDto) {
        Tag tag = new Tag();
        tag.setName(tagDto.getName());

        tagService.saveTag(tag);
    }

    @DeleteMapping()
    public void deleteTag(@RequestParam String name) {
        Tag tag = tagService.findTagByName(name);

        tagService.deleteTag(tag);
    }
}
