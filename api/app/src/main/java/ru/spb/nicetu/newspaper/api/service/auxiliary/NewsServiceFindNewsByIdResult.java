package ru.spb.nicetu.newspaper.api.service.auxiliary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.spb.nicetu.newspaper.api.model.News;

/**
 * <p>Result of request for concrete news</p>
 * 
 * <p>Contains {@code News} with first content and comment page.</p>
 * <p>Also contains {@code Boolean}s to indicate if pages were last.</p>
    * @author Nikita Osiptsov
    * @see {@link News}
    * @see {@link NewsService}
 * @since 1.0
 */
@Setter
@Getter
@AllArgsConstructor
@ToString
public class NewsServiceFindNewsByIdResult {
    private News news;
    private Boolean isLastContentPage;
    private Boolean isLastCommentsPage;
}
