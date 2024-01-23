package ru.spb.nicetu.newspaper.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.spb.nicetu.newspaper.api.model.image.FileSystemImage;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.OffsetDateTime;
import java.util.Collection;

/**
 * <p>Represents news.</p>
 *
 * <p>Contains id, title, post time, image content, comments and liked users.</p>
    * @author Nikita Osiptsov
 * @since 1.0
 */
@Entity
@DynamicInsert
@Table(name = "news", schema = "subject")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "newsid")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "posttime")
    private OffsetDateTime postTime;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(fetch = FetchType.LAZY,
        mappedBy = "news",
        cascade = CascadeType.ALL)
    private FileSystemImage image;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.LAZY,
        mappedBy = "news",
        cascade = CascadeType.ALL)
    private Collection<NewsContentBlock> contentBlocks;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.LAZY,
        mappedBy = "news",
        cascade = CascadeType.ALL)
    private Collection<Comment> comments;

    @ManyToMany(mappedBy = "news", fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    private Collection<Tag> tags;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "likedNews")
    private Collection<User> likers;
}
