package ru.spb.nicetu.newspaper.api.model.image;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.PreRemove;

import org.springframework.core.io.Resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.spb.nicetu.newspaper.api.model.News;

/**
 * <p>Abstract class used to represent {@code News} picture.</p>
 *
 * <p>Contains id of owning {@code News} and MIME type.</p>
 * <p>It is up for implementation to define way to store image (by implementing {@code asResource()} method).</p>
    * @author Nikita Osiptsov
    * @see {@link News}
 * @since 1.0
 */
@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractImage {
    @Id
    @Column(name = "newsid")
    protected Long newsId;

    @Column(name = "type")
    protected String type;

    @OneToOne
    @JoinColumn(name = "newsid")
    @MapsId("newsId")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    protected News news;

    @PreRemove
    protected void breakAssociation() {
        this.news.setImage(null);
    }

    public abstract Resource asResource();
}
