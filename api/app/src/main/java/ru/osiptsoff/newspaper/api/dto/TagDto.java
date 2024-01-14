package ru.osiptsoff.newspaper.api.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.osiptsoff.newspaper.api.model.Tag;

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
