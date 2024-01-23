package ru.spb.nicetu.newspaper.api.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>Data Transfer Object for request for page of {@code News}.</p>
 *
 * <p>Contains id of owning news and number of page.</p>
    * @author Nikita Osiptsov
    * @see {@link News}
 * @since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDtoNoOwner {
    @NotNull
    private Integer pageNumber;
}
