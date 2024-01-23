package ru.spb.nicetu.newspaper.api.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>Data Transfer Object for tags which user is associated with.</p>
 *
 * <p>List of liked tags and list of disliked tags.</p>
    * @author Nikita Osiptsov
    * @see {@link TagDto}
 * @since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTagListDto {
    private List<TagDto> liked;
    private List<TagDto> disliked;
}
