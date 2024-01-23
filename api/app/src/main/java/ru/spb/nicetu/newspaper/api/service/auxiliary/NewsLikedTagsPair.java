package ru.spb.nicetu.newspaper.api.service.auxiliary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.spb.nicetu.newspaper.api.model.News;

/**
 * <p>Pair of {@code News} and number of tags of this news liked by user.</p>
    * @author Nikita Osiptsov
    * @see {@link News}
 * @since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsLikedTagsPair  {
    private News news;
    private Long likedTags;
}
