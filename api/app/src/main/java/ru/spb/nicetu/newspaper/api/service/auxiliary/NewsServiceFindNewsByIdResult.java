package ru.spb.nicetu.newspaper.api.service.auxiliary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.spb.nicetu.newspaper.api.model.News;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class NewsServiceFindNewsByIdResult {
    private News news;
    private Boolean isLastContentPage;
    private Boolean isLastCommentsPage;
}
