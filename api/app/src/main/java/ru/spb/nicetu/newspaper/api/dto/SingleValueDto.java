package ru.spb.nicetu.newspaper.api.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>Generic Data Transfer Object for request for single value.</p>
 *
 * <p>Contains single value.</p>
    * @author Nikita Osiptsov
 * @since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SingleValueDto<T> {
    @NotNull
    private T value;
}
