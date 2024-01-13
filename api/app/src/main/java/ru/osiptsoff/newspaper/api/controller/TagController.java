package ru.osiptsoff.newspaper.api.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.osiptsoff.newspaper.api.dto.TagDto;
import ru.osiptsoff.newspaper.api.model.Tag;
import ru.osiptsoff.newspaper.api.service.TagService;

@RestController
@RequestMapping("/tag")
@ResponseStatus(HttpStatus.NO_CONTENT)
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @PostMapping()
    public void saveTag(@Valid @RequestBody TagDto tagDto) {
        Tag tag = new Tag();
        tag.setName(tagDto.getName());

        tagService.saveTag(tag);
    }

    @DeleteMapping()
    public void deleteTag(@Valid @RequestBody TagDto tagDto) {
        Tag tag = tagService.findTagByName(tagDto.getName());

        tagService.deleteTag(tag);
    }
}
