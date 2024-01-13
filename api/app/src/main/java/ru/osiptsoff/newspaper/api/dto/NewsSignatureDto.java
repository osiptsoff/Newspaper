package ru.osiptsoff.newspaper.api.dto;

import java.time.OffsetDateTime;
import java.util.Collection;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.osiptsoff.newspaper.api.model.News;
import ru.osiptsoff.newspaper.api.model.Tag;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsSignatureDto {
    @NotBlank
    @Size(max=255)
    private String title;
    private Integer id;
    private String picture;
    private OffsetDateTime postTime;
    private Collection<Tag> tags;

    public static NewsSignatureDto from(News news) {
        NewsSignatureDto dto = new NewsSignatureDto();

        dto.id = news.getId();
        dto.title = news.getTitle();
        dto.picture = news.getPicturePath();
        dto.postTime = news.getPostTime();
        dto.tags = news.getTags();

        return dto;
    }
}
