package ru.osiptsoff.newspaper.api.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class NewsContentBlockId implements Serializable {
    @Column(name = "newsid")
    private Integer newsId;

    @Column(name = "number")
    private Integer number;
}