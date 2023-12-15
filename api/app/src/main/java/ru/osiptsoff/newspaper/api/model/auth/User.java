package ru.osiptsoff.newspaper.api.model.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.osiptsoff.newspaper.api.model.News;
import ru.osiptsoff.newspaper.api.model.Tag;
import ru.osiptsoff.newspaper.api.model.UserTag;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Collection;
import java.util.stream.Collectors;

@Entity
@Table(name = "auth.user")
@Setter
@Getter
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "userid")
    private Integer id;

    @Column(name = "login")
    private String login;

    @Column(name = "passwhash")
    private String password;

    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL)
    private Token token;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Collection<UserTag> tags;

    @ManyToMany
    @JoinTable(
        name = "auth.user_role",
        joinColumns = @JoinColumn(name = "userid"),
        inverseJoinColumns = @JoinColumn(name = "roleid")
    )
    private Collection<Role> roles;

    @ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinTable(
        name = "subject.user_liked_news",
        joinColumns = @JoinColumn(name = "userid"),
        inverseJoinColumns = @JoinColumn(name = "newsid")
    )
    private Collection<News> likedNews;

    public Collection<Tag> getLikedTags() {
        return tags.stream()
            .filter( rel -> rel.getLikes() == true )
            .map( rel -> rel.getTag() )
            .collect( Collectors.toSet() );
    }

    public Collection<Tag> getDislikedTags() {
        return tags.stream()
            .filter( rel -> rel.getLikes() == false )
            .map( rel -> rel.getTag() )
            .collect( Collectors.toSet() );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
