package ru.osiptsoff.newspaper.api.service.auxiliary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.osiptsoff.newspaper.api.model.News;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class NewsServiceFindNewsByIdResponse {
    private News news;
    private Boolean isLastContentPage;
    private Boolean isLastCommentsPage;
}
