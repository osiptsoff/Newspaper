package ru.spb.nicetu.newspaper.api.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>Data Transfer Object for request to authenticate user.</p>
 *
 * <p>Contains login and password of user.</p>
    * @author Nikita Osiptsov
 * @since 1.0
 */
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
