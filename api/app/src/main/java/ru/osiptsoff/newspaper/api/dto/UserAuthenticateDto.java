package ru.osiptsoff.newspaper.api.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthenticateDto {
    @NotBlank
    @Size(max = 55)
    private String login;
    @NotBlank
    private String password;
}
