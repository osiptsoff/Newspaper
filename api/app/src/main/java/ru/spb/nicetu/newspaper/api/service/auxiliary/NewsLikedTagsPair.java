package ru.spb.nicetu.newspaper.api.service.auxiliary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.spb.nicetu.newspaper.api.model.News;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsLikedTagsPair  {
    private News news;
    private Long likedTags;
}
