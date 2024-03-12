package ru.spb.nicetu.newspaper.api.controller;

import java.util.List;

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
import ru.spb.nicetu.newspaper.api.service.facade.TagServiceFacade;

/**
 * <p>Controller for '/tag' endpoint.</p>
 *
 * <p>Provides API for posting, updating, deletion and reading tags.</p>
    * @author Nikita Osiptsov
    * @see {@link TagServiceFacade}
 * @since 1.0
 */
@RestController
@RequestMapping("/tag")
@ResponseStatus(HttpStatus.NO_CONTENT)
@RequiredArgsConstructor
public class TagController {
    private final TagServiceFacade tagServiceFacade;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<TagDto> findAllTags() {
        return tagServiceFacade.findAllTags();
    }

    @PostMapping()
    public void saveTag(@Valid @RequestBody TagDto tagDto) {
        tagServiceFacade.saveTag(tagDto);
    }

    @DeleteMapping()
    public void deleteTag(@RequestParam String name) {
        tagServiceFacade.deleteTag(name);
    }
}
