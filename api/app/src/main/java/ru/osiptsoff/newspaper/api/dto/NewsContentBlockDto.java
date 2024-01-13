package ru.osiptsoff.newspaper.api.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.osiptsoff.newspaper.api.model.NewsContentBlock;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsContentBlockDto {
    @NotNull
    private Integer blockNumber;
    @NotBlank
    @Size(max=255)
    private String text;

    public static NewsContentBlockDto from(NewsContentBlock block) {
        NewsContentBlockDto result = new NewsContentBlockDto();

        result.setText(block.getText());
        result.setBlockNumber(block.getNewsContentBlockId().getNumber());

        return result;
    }
}
