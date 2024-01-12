package ru.osiptsoff.newspaper.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserNewsDto {
    private String login;
    private Integer newsId;
}
