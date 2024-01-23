package ru.spb.nicetu.newspaper.api.model.embeddable;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.spb.nicetu.newspaper.api.model.UserTag;

/**
 * <p>Composite database id for {@code UserTag}.</p>
 *
 * <p>Contains ids of related {@code User} and {@code Tag}.</p>
    * @author Nikita Osiptsov
    * @see {@link UserTag}
 * @since 1.0
 */
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