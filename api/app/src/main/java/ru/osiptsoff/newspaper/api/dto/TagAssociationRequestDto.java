package ru.osiptsoff.newspaper.api.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagAssociationRequestDto {
    @NotBlank
    private String tag;
    private Boolean belongs;
}
