package ru.osiptsoff.newspaper.api.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Embeddable
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
class UserTagId implements Serializable {
    @Column(name = "userid")
    private Integer userId;

    @Column(name = "tagid")
    private Integer tagId;
}

@Entity
@Table(name = "subject.user_tag")
@Setter
@Getter
@NoArgsConstructor
public class UserTag {
    @EmbeddedId
    private UserTagId userTagId;

    @ManyToOne
    @JoinColumn(name = "userid")
    @MapsId("userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "tagid")
    @MapsId("tagId")
    private Tag tag;

    @Column(name = "likes")
    private Boolean likes;
}
