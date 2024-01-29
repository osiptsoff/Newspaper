package ru.spb.nicetu.newspaper.api.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>Data Transfer Object for id.</p>
 *
 * <p>Contains only one {@code Integer} value: id.</p>
    * @author Nikita Osiptsov
 * @since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdDto {
    @NotNull
    private Long id;
}
