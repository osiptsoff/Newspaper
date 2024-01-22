package ru.spb.nicetu.newspaper.api.model.embeddable;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTagId implements Serializable {
    @Column(name = "userid")
    private Long userId;

    @Column(name = "tagid")
    private Long tagId;
}