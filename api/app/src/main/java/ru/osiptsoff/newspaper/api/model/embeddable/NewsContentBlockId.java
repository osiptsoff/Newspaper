package ru.osiptsoff.newspaper.api.model.embeddable;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsContentBlockId implements Serializable {
    @Column(name = "newsid")
    private Integer newsId;

    @Column(name = "number")
    private Integer number;
}