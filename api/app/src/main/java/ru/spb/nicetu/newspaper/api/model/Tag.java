package ru.spb.nicetu.newspaper.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Collection;

/**
 * <p>Represents tag.</p>
 *
 * <p>Contains id, name, tagged news and associated users.</p>
    * @author Nikita Osiptsov
 * @since 1.0
 */
@Entity
@Table(name = "tag", schema = "subject")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tagid")
    private Long id;

    @Column(name = "name")
    private String name;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany
    @JoinTable(name = "news_tag",
        schema = "subject",
        joinColumns = @JoinColumn(name = "tagid"),
        inverseJoinColumns = @JoinColumn(name = "newsid"))
    private Collection<News> news;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL)
    private Collection<UserTag> users;
}
