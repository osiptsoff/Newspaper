package ru.spb.nicetu.newspaper.api.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>Data Transfer Object for request for page of {@code Comment}s or {@code NewsContentBlock}s.</p>
 *
 * <p>Contains id of owning news and number of page.</p>
    * @author Nikita Osiptsov
    * @see {@link Comment}
    * @see {@link NewsContentBlock}
 * @since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDto {
    @NotNull
    private Long ownerId;
    @NotNull
    private Integer pageNumber;
}
