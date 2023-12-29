package ru.osiptsoff.newspaper.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
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
import javax.persistence.Table;
import java.time.OffsetDateTime;
import java.util.Collection;

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
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "picturepath")
    private String picturePath;

    @Column(name = "posttime")
    private OffsetDateTime postTime;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "news",
            cascade = CascadeType.ALL
    )
    private Collection<NewsContentBlock> content;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "news",
            cascade = CascadeType.ALL
    )
    private Collection<Comment> comments;

    @ManyToMany(mappedBy = "news", fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    private Collection<Tag> tags;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "likedNews")
    private Collection<User> likers;
}
