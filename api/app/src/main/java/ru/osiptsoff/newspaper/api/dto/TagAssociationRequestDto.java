package ru.osiptsoff.newspaper.api.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
