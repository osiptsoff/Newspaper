package ru.spb.nicetu.newspaper.api.environment;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.spb.nicetu.newspaper.api.repository.TagRepository;
import ru.spb.nicetu.newspaper.api.service.TagServiceImpl;

@Component
@RequiredArgsConstructor
@Getter
public class TagServiceTestEnvironment {
    private final TagRepository tagRepository;
    private final TagServiceImpl tagService;
}
