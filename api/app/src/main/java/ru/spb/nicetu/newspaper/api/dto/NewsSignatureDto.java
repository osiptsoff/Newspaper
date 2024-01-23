package ru.spb.nicetu.newspaper.api.dto;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.spb.nicetu.newspaper.api.model.News;

/**
 * <p>Data Transfer Object for {@code News}, contains only general information.</p>
 *
 * <p>Contains title of news, its id, post time and tags.</p>
 * <p>This class also provides static method for generating DTO from {@code News} instance</p>
    * @author Nikita Osiptsov
    * @see {@link News}
 * @since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class NewsSignatureDto {
    @NotBlank
    @Size(max=255)
    private String title;
    private Long id;
    private OffsetDateTime postTime;
    private Collection<TagDto> tags;

    public static NewsSignatureDto from(News news) {
        NewsSignatureDto dto = new NewsSignatureDto();

        dto.id = news.getId();
        dto.title = news.getTitle();
        dto.postTime = news.getPostTime();
        dto.tags = news.getTags()
            .stream()
            .map(t -> new TagDto(t.getName()))
            .collect(Collectors.toSet());

        return dto;
    }
}
