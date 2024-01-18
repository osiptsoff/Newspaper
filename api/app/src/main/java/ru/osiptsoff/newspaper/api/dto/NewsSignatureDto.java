package ru.osiptsoff.newspaper.api.dto;

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
import ru.osiptsoff.newspaper.api.model.News;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class NewsSignatureDto {
    @NotBlank
    @Size(max=255)
    private String title;
    private Integer id;
    private OffsetDateTime postTime;
    private Collection<TagDto> tags;

    public static NewsSignatureDto from(News news) {
        NewsSignatureDto dto = new NewsSignatureDto();

        dto.id = news.getId();
        dto.title = news.getTitle();
        dto.postTime = news.getPostTime();
        dto.tags = news
                    .getTags()
                    .stream()
                    .map( t -> new TagDto(t.getName()) )
                    .collect(Collectors.toSet());

        return dto;
    }
}
