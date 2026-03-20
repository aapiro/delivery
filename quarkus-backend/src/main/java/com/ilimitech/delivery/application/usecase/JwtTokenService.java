package com.ilimitech.delivery.application.usecase;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;

@ApplicationScoped
public class JwtTokenService {

    @ConfigProperty(name = "mp.jwt.verify.issuer", defaultValue = "delivery-auth")
    String issuer;

    @ConfigProperty(name = "delivery.auth.access-token-ttl-minutes", defaultValue = "15")
    long accessTokenTtlMinutes;

    public String createAccessToken(Long userId, String email, String fullName) {
        Instant expiresAt = Instant.now().plus(Duration.ofMinutes(accessTokenTtlMinutes));
        return Jwt.issuer(issuer)
                .subject(String.valueOf(userId))
                .upn(email)
                .groups(Set.of("user"))
                .claim("email", email)
                .claim("fullName", fullName != null ? fullName : "")
                .expiresAt(expiresAt)
                .sign();
    }
}
