package ru.spb.nicetu.newspaper.api.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeTagDto {
    @NotBlank
    @Size(max=25)
    private String name;
    private Boolean liked;
}
