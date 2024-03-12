package ru.spb.nicetu.newspaper.api.environment;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.spb.nicetu.newspaper.api.repository.TagRepository;
import ru.spb.nicetu.newspaper.api.service.TagServiceImpl;
import ru.spb.nicetu.newspaper.api.test.TagServiceTests;

/**
 * <p>Environment used in {@code TagServiceTests}.</p>
 *
 * <p>Contains tested service, repositories to load related resources, testing entities.</p>
    * @author Nikita Osiptsov
    * @see {@link TagServiceTests}
 * @since 1.2
 */
@Component
@RequiredArgsConstructor
@Getter
public class TagServiceTestEnvironment {
    private final TagRepository tagRepository;
    private final TagServiceImpl tagService;
}
