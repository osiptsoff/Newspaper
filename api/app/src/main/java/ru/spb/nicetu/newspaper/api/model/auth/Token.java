package ru.spb.nicetu.newspaper.api.model.auth;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.PreRemove;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.spb.nicetu.newspaper.api.model.User;

/**
 * <p>Class for refresh token.</p>
 *
 * <p>Used for storing and subsequent comparison of refresh tokens</p>
 * <p>Contains owning {@code User} and token value.</p>
    * @author Nikita Osiptsov
    * @see {@link User}
 * @since 1.0
 */
@Entity
@Table(name = "token", schema = "auth")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    @Id
    @Column(name = "userid")
    private Long userId;

    @Column(name = "value")
    private String value;

    @OneToOne
    @JoinColumn(name = "userid")
    @MapsId("userId")
    private User owner;

    @PreRemove
    private void breakAssociation() {
        owner.setToken(null);
    }
}
