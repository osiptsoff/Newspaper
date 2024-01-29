package ru.spb.nicetu.newspaper.api.model.auth;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import ru.spb.nicetu.newspaper.api.model.User;

/**
 * <p>Spring Security compatible adapter for {@code User}.</p>
 *
 * <p>Used to separate {@code User} logic and Spring Security {@code UserDetails} implementation.</p>
 * <p>Contains associated {@code User}, implements {@code UserDetails} methods.</p>
    * @author Nikita Osiptsov
    * @see {@link User}
    * @see {@link UserDetails}
 * @since 1.0
 */
@AllArgsConstructor
public class UserPrincipal implements UserDetails {
    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getLogin();
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
