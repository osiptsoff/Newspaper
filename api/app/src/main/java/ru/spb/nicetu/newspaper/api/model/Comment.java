package ru.spb.nicetu.newspaper.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.OffsetDateTime;

/**
 * <p>Represents comment.</p>
 *
 * <p>Contains id, text, post time, author and owning {@code News}.</p>
    * @author Nikita Osiptsov
    * @see {@link News}
 * @since 1.0
 */
@Entity
@DynamicInsert
@Table(name = "comment", schema = "subject")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commentid")
    private Long id;

    @Column(name = "text")
    private String text;

    @Column(name = "posttime")
    private OffsetDateTime postTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private User author;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "newsid")
    private News news;
}