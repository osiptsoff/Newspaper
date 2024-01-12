package ru.osiptsoff.newspaper.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.osiptsoff.newspaper.api.model.NewsContentBlock;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsContentBlockDto {
    private Integer blockNumber;
    private String text;

    public static NewsContentBlockDto from(NewsContentBlock block) {
        NewsContentBlockDto result = new NewsContentBlockDto();

        result.setText(block.getText());
        result.setBlockNumber(block.getNewsContentBlockId().getNumber());

        return result;
    }
}
