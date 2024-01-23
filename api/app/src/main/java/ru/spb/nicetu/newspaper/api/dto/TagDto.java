package ru.spb.nicetu.newspaper.api.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.spb.nicetu.newspaper.api.model.Tag;

/**
 * <p>Data Transfer Object for {@code Tag}.</p>
 *
 * <p>Contains name of tag.</p>
 * <p>This class also provides static method for generating DTO from {@code Tag} instance</p>
    * @author Nikita Osiptsov
    * @see {@link Tag}
 * @since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagDto {
    @NotBlank
    @Size(max=25)
    private String name;

    public static TagDto from(Tag tag) {
        TagDto result = new TagDto();

        result.setName(tag.getName());

        return result;
    } 
}
