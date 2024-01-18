package ru.osiptsoff.newspaper.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.osiptsoff.newspaper.api.model.auth.Role;
import ru.osiptsoff.newspaper.api.model.auth.Token;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "user", schema = "auth")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userid")
    private Long id;

    @Column(name = "login")
    private String login;

    @Column(name = "passwhash")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "lastname")
    private String lastName;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL)
    private Token token;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Collection<UserTag> tags;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_role",
        schema = "auth",
        joinColumns = @JoinColumn(name = "userid"),
        inverseJoinColumns = @JoinColumn(name = "roleid")
    )
    private Collection<Role> roles;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinTable(
        name = "user_liked_news",
        schema = "subject",
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
}
