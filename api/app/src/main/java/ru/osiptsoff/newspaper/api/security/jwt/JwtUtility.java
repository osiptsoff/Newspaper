package ru.osiptsoff.newspaper.api.security.jwt;

import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Setter;
import ru.osiptsoff.newspaper.api.model.User;
import ru.osiptsoff.newspaper.api.model.auth.Role;
import ru.osiptsoff.newspaper.api.model.auth.UserPrincipal;

@Component
public class JwtUtility {
    private final SecretKey accessKey;
    private final SecretKey refreshKey;

    @Value("${app.config.security.accessLifespawnSec}")
    @Setter
    private Long accesLifespawn;
    @Value("${app.config.security.refreshLifespawnSec}")
    @Setter
    private Long refreshLifespawn;

    public JwtUtility(  @Value("${app.config.security.accessSecret}") String base64AccessKey,
                        @Value("${app.config.security.refreshSecret}") String base64RefreshKey) {
        this.accessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64AccessKey));
        this.refreshKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64RefreshKey));
    }

    public String generateRefreshToken(UserDetails userPrincipal) {
        return generateToken(userPrincipal, refreshKey, refreshLifespawn);
    }

    public String generateAccessToken(UserDetails userPrincipal) {
        return generateToken(userPrincipal, accessKey, accesLifespawn);
    }

    public UserPrincipal parseAndValidateRefreshToken(String refreshToken) {
        return generateUserPrincipal(refreshToken, refreshKey);
    }

    public UserPrincipal parseAndValidateAccessToken(String accessToken) {
        return generateUserPrincipal(accessToken, accessKey);
    }

    private UserPrincipal generateUserPrincipal(String token, SecretKey key) {
        Claims claims = parseToken(token, key);

        if(claims == null) {
            return null;
        }
        if(!areClaimsValid(claims)) {
            return null;
        }

        User user = new User();
        user.setLogin(claims.getSubject());
        user.setRoles(claims
            .getAudience()
            .stream()
            .map( a -> {
                    Role role = new Role();
                    role.setName(a);
                    return role;
                })
            .collect(Collectors.toSet())
        );

        return new UserPrincipal(user);
    }

    private String generateToken(UserDetails userPrincipal, SecretKey key, Long lifespawn) {
        Date now = new Date();

        return Jwts.builder()
            .issuedAt(now)
            .expiration(new Date(now.getTime() + lifespawn * 1000))
            .subject(userPrincipal.getUsername())
            .audience()
            .add(userPrincipal.getAuthorities()
                    .stream()
                    .map( a -> a.getAuthority())
                    .collect(Collectors.toSet())
            )
            .and()
            .signWith(key)
            .compact();
    }

    private Claims parseToken(String token, SecretKey key) {
        try {
            return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        } catch(JwtException | IllegalArgumentException e) {
            return null;
        }
        
    }

    private boolean areClaimsValid(Claims claims) {
        return claims.getIssuedAt() != null
            && claims.getExpiration() != null
            && claims.getSubject() != null
            && claims.getAudience() != null
            && claims.getExpiration().compareTo(new Date()) > 0;
    }
}
