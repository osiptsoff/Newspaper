package ru.spb.nicetu.newspaper.api.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>Data Transfer Object for request to associate news with {@code Tag}.</p>
 *
 * <p>Contains name of tag and {@code Boolean} value: {@code true} if news belongs to tag,
 * {@code false} otherwise.</p>
    * @author Nikita Osiptsov
    * @see {@link Tag}
 * @since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagAssociationRequestDto {
    @NotBlank
    @Size(max=25)
    private String tag;
    @NotNull
    private Boolean belongs;
}
