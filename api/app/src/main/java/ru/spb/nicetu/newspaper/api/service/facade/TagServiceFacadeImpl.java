package ru.spb.nicetu.newspaper.api.service.facade;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ru.spb.nicetu.newspaper.api.dto.TagDto;
import ru.spb.nicetu.newspaper.api.model.Tag;
import ru.spb.nicetu.newspaper.api.service.TagService;

/**
 * <p>{@link TagServiceFacade} implementation.</p>
 * 
    * @author Nikita Osiptsov
    * @see {@link TagService}
 * @since 1.2
 */
@Component
@RequiredArgsConstructor
public class TagServiceFacadeImpl implements TagServiceFacade {
    private final TagService tagService;

    @Override
    public List<TagDto> findAllTags() {
        return tagService.findAllTags()
            .stream()
            .map(t -> TagDto.from(t))
            .collect(Collectors.toList());
    }

    @Override
    public void saveTag(TagDto tagDto) {
        Tag tag = new Tag();
        tag.setName(tagDto.getName());

        tagService.saveTag(tag);
    }

    @Override
    public void deleteTag(String name) {
        Tag tag = tagService.findTagByName(name);

        tagService.deleteTag(tag);
    }
    
}
