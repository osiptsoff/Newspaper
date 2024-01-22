package ru.spb.nicetu.newspaper.api.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.spb.nicetu.newspaper.api.model.embeddable.UserTagId;

@Entity
@Table(name = "user_tag", schema = "subject")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
