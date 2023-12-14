package ru.osiptsoff.newspaper.api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
import java.util.Collection;

@Entity
@Table(name = "subject.news")
@Setter
@Getter
@NoArgsConstructor
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "newsid")
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "picturepath")
    private String picturePath;

    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "news",
            cascade = CascadeType.ALL
    )
    private Collection<NewsContentBlock> content;

    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "news",
            cascade = CascadeType.ALL
    )
    private Collection<Comment> comments;

    @ManyToMany(mappedBy = "news")
    Collection<Tag> tags;
}
