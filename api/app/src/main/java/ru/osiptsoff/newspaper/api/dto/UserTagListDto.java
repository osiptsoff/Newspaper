package ru.osiptsoff.newspaper.api.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTagListDto {
    private List<TagDto> liked;
    private List<TagDto> disliked;
}