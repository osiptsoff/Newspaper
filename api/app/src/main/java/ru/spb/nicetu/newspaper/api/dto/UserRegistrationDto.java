package ru.spb.nicetu.newspaper.api.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>Data Transfer Object for request to register user.</p>
 *
 * <p>Contains login and password of user as well as their name and lastname.</p>
    * @author Nikita Osiptsov
 * @since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDto {
    @NotBlank
    @Size(max = 55)
    private String login;
    @NotBlank
    private String password;

    @NotBlank
    @Size(max = 55)
    private String name;

    @NotBlank
    @Size(max = 55)
    private String lastName;
}
