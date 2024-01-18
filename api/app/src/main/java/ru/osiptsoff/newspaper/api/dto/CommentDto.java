package ru.osiptsoff.newspaper.api.dto;

import java.time.OffsetDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.osiptsoff.newspaper.api.model.Comment;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class CommentDto {
    @NotBlank
    @Size(max=255)
    private String text;
    private Long newsId;
    private Long id;
    @Size(max=55)
    private String author;
    private OffsetDateTime postTime;

    public static CommentDto from(Comment comment) {
        CommentDto commentDto = new CommentDto();

        commentDto.text = comment.getText();
        commentDto.newsId = comment.getNews().getId();
        commentDto.id = comment.getId();
        commentDto.author = comment.getAuthor().getName() == null || comment.getAuthor() == null 
                                ? comment.getAuthor().getLogin()
                                : comment.getAuthor().getName() + " " + comment.getAuthor().getLastName();
        commentDto.postTime = comment.getPostTime();

        return commentDto;
    }
}
