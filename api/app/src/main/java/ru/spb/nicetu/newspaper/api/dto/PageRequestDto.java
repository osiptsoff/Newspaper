package ru.spb.nicetu.newspaper.api.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDto {
    @NotNull
    private Long ownerId;
    @NotNull
    private Integer pageNumber;
}
