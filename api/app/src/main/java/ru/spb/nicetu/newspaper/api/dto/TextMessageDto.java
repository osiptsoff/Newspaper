package ru.spb.nicetu.newspaper.api.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>Data Transfer Object for text message.</p>
 *
 * <p>Contains single {@code String}.</p>
    * @author Nikita Osiptsov
 * @since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TextMessageDto {
    @NotBlank
    private String message;
}
