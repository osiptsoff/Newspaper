package ru.osiptsoff.newspaper.api.model.auth;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "auth.role")
@Setter
@Getter
@NoArgsConstructor
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "roleid")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }
    
}
