package ru.osiptsoff.newspaper.api.model.auth;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.osiptsoff.newspaper.api.model.User;

@Entity
@Table(name = "token", schema = "auth")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    @Id
    @Column(name = "userid")
    private Integer userId;

    @Column(name = "value")
    private String value;

    @OneToOne
    @JoinColumn(name = "userid")
    @MapsId("userId")
    private User owner;
}
