package ru.auth.jwt;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtility {
    private final SecretKey accessKey;
    private final SecretKey refreshKey;

    private Long accesLifespawn;
    private Long refreshLifespawn;

    public JwtUtility(  @Value("${app.config.security.accessSecret}") String base64AccessKey,
                        @Value("${app.config.security.refreshSecret}") String base64RefreshKey) {
        this.accessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessSecret));
        this.refreshKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshSecret));
    }
}
