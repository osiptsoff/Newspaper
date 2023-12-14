package ru.osiptsoff.newspaper.api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.osiptsoff.newspaper.api.model.auth.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "subject.comment")
@Setter
@Getter
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "commentid")
    private Integer id;

    @Column(name = "text")
    private String text;

    @Column(name = "posttime")
    private Date postTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "newsid")
    private News news;
}
