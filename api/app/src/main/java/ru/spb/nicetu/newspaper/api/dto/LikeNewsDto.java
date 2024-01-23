package ru.spb.nicetu.newspaper.api.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>Data Transfer Object for request to like {@code News}.</p>
 *
 * <p>Contains id of news and {@code Boolean} value: {@code true} if user likes news
 *  or {@code false} if previously posted like must be undone.</p>
    * @author Nikita Osiptsov
 * @since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeNewsDto {
    @NotNull
    private Long newsId;
    @NotNull
    private Boolean liked;
}
