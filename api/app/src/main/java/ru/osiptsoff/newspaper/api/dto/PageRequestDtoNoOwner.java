package ru.osiptsoff.newspaper.api.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDtoNoOwner {
    @NotNull
    private Integer pageNumber;
}
