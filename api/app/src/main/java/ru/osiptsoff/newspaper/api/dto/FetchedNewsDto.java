package ru.osiptsoff.newspaper.api.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FetchedNewsDto {
    private NewsSignatureDto newsSignature;
    private List<CommentDto> comments;
    private List<NewsContentBlockDto> content;

    private Integer likesCount;

    private Boolean isLastCommentsPage;
    private Boolean isLastContentPage;
}
