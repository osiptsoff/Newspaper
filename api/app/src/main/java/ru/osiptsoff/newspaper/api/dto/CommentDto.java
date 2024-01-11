package ru.osiptsoff.newspaper.api.dto;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.osiptsoff.newspaper.api.model.Comment;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private String text;
    private Integer newsId;

    @JsonIgnoreProperties(ignoreUnknown = true)
    private Integer id;
    @JsonIgnoreProperties(ignoreUnknown = true)
    private String author;
    @JsonIgnoreProperties(ignoreUnknown = true)
    private OffsetDateTime postTime;

    public static CommentDto from(Comment comment) {
        CommentDto commentDto = new CommentDto();

        commentDto.text = comment.getText();
        commentDto.newsId = comment.getNews().getId();
        commentDto.id = comment.getId();
        commentDto.author = comment.getAuthor().getLogin();
        commentDto.postTime = comment.getPostTime();

        return commentDto;
    }
}
