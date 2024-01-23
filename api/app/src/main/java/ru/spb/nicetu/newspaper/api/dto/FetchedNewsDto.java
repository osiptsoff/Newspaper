package ru.spb.nicetu.newspaper.api.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>Data Transfer Object for fully fetched {@code News}.</p>
 *
 * <p>Contains {@code NewsSignatureDto} of news signature,
 *  list of {@code CommentDto} of comments and list of {@code NewsContentBlockDto} of news content.</p>
    * @author Nikita Osiptsov
    * @see {@link Comment}
    * @see {@link NewsSignatureDto}
    * @see {@link CommentDto}
    * @see {@link NewsContentBlockDto}
 * @since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FetchedNewsDto {
    private NewsSignatureDto newsSignature;
    private List<CommentDto> comments;
    private List<NewsContentBlockDto> content;

    private Long likesCount;

    private Boolean isLastCommentsPage;
    private Boolean isLastContentPage;
}
