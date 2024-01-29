package ru.spb.nicetu.newspaper.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.spb.nicetu.newspaper.api.model.embeddable.NewsContentBlockId;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

/**
 * <p>Represents block of {@code News} content.</p>
 *
 * <p>Contains owning news, number of block and text.</p>
    * @author Nikita Osiptsov
    * @see {@link News}
 * @since 1.0
 */
@Entity
@Table(name = "textchunk", schema = "subject")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsContentBlock {
    @EmbeddedId
    private NewsContentBlockId newsContentBlockId;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "newsid")
    @MapsId("newsId")
    private News news;

    @Column(name = "text")
    private String text;
}
