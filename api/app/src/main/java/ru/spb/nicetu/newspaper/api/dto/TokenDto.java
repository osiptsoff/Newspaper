package ru.spb.nicetu.newspaper.api.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>Data Transfer Object for Token.</p>
 *
 * <p>Contains type of token (access or refresh) and its value.</p>
    * @author Nikita Osiptsov
    * @see {@link Comment}
 * @since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {
    private String type;
    @NotBlank
    private String value;
}
