package ru.osiptsoff.newspaper.api.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import java.io.Serializable;

@Embeddable
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
class NewsContentBlockId implements Serializable {
    @Column(name = "newsid")
    private Integer newsId;

    @Column(name = "number")
    private Integer number;
}

@Entity
@Table(name = "subject.chunk")
@Setter
@Getter
@NoArgsConstructor
public class NewsContentBlock {
    @EmbeddedId
    private NewsContentBlockId newsContentBlockId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "newsid")
    @MapsId("newsId")
    private News news;

    @Column(name = "text")
    private String text;
}
