package ru.spb.nicetu.newspaper.api.service.facade;

import java.util.List;

import ru.spb.nicetu.newspaper.api.dto.TagDto;
import ru.spb.nicetu.newspaper.api.service.TagService;

/**
 * <p>Class which wraps {@link TagService} in order to provide
 * simplier interface for interaction</p>
 * 
 * <p>Minimizes client dependencies; converts DTOs to domain objects, converts result domain objects to DTOs</p>
    * @author Nikita Osiptsov
    * @see {@link TagService}
 * @since 1.2
 */
public interface TagServiceFacade {
    List<TagDto> findAllTags();
    void saveTag(TagDto tagDto);
    void deleteTag(String name);
}
