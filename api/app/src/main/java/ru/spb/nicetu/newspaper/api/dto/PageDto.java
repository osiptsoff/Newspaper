package ru.spb.nicetu.newspaper.api.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>Generic Data Transfer Object for page.</p>
 *
 * <p>Contains list of page content, and {@code Boolean}, signalizing if given page is last.</p>
    * @author Nikita Osiptsov
 * @since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDto<T> {
    private List<T> content;
    private Boolean isLast;
}
