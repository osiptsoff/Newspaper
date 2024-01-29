package ru.spb.nicetu.newspaper.api.model.auth;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.security.core.GrantedAuthority;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>Class for user's role.</p>
 *
 * <p>Contains id and name of role.</p>
    * @author Nikita Osiptsov
 * @since 1.0
 */
@Entity
@Table(name = "role", schema = "auth")
@Data
@NoArgsConstructor
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "roleid")
    private Long id;

    @Column(name = "name")
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }
    
}
