package ru.spb.nicetu.newspaper.api.dto;

import java.time.OffsetDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.spb.nicetu.newspaper.api.model.Comment;

/**
 * <p>Data Transfer Object for {@code Comment}.</p>
 *
 * <p>Contains text, id of associated news, id of comment, name of author and post time;
 * name of author is defined as: login, if database contains no name or lastname of user,
 * name and lastname otherwise.</p>
 * <p>This class also provides static method for generating DTO from {@code Comment} instance</p>
    * @author Nikita Osiptsov
    * @see {@link Comment}
 * @since 1.0
 */
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
