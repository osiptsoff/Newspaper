package ru.spb.nicetu.newspaper.api.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>Data Transfer Object for request to associate user with {@code Tag}.</p>
 *
 * <p>Contains name of tag and {@code Boolean} value: {@code true} if user likes tag,
 *  {@code false} if user dislikes tag of {@code null} if previously created association must be broken.</p>
    * @author Nikita Osiptsov
 * @since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeTagDto {
    @NotBlank
    @Size(max=25)
    private String name;
    private Boolean liked;
}
