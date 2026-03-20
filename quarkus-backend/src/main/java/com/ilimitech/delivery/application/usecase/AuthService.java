package com.ilimitech.delivery.application.usecase;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.ilimitech.delivery.application.port.out.RefreshTokenRepository;
import com.ilimitech.delivery.application.port.out.UserRepository;
import com.ilimitech.delivery.infrastructure.adapter.out.persistence.RefreshTokenEntity;
import com.ilimitech.delivery.infrastructure.adapter.out.persistence.User;
import com.ilimitech.delivery.infrastructure.security.TokenHasher;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class AuthService {

    @Inject
    UserRepository userRepository;

    @Inject
    RefreshTokenRepository refreshTokenRepository;

    @Inject
    JwtTokenService jwtTokenService;

    @ConfigProperty(name = "delivery.auth.refresh-token-days", defaultValue = "30")
    long refreshTokenDays;

    @Transactional
    public AuthTokens register(String name, String email, String password) {
        String normEmail = normalizeEmail(email);
        if (userRepository.findByEmail(normEmail).isPresent()) {
            throw conflict("Email already registered");
        }
        User user = new User();
        user.setFullName(name != null ? name.trim() : "");
        user.setEmail(normEmail);
        user.setPasswordHash(BCrypt.withDefaults().hashToString(12, password.toCharArray()));
        user.setUserType("CUSTOMER");
        user.setIsActive(true);
        user.setIsVerified(false);
        userRepository.persist(user);
        return issueTokens(user);
    }

    @Transactional
    public AuthTokens login(String email, String password) {
        Optional<User> opt = userRepository.findByEmail(email);
        if (opt.isEmpty()) {
            throw unauthorized("Invalid credentials");
        }
        User user = opt.get();
        if (Boolean.FALSE.equals(user.getIsActive())) {
            throw unauthorized("Invalid credentials");
        }
        if (user.getPasswordHash() == null
                || !BCrypt.verifyer().verify(password.toCharArray(), user.getPasswordHash()).verified) {
            throw unauthorized("Invalid credentials");
        }
        return issueTokens(user);
    }

    @Transactional
    public AuthTokens refresh(String refreshTokenPlain) {
        String hash = TokenHasher.sha256Hex(refreshTokenPlain);
        Optional<RefreshTokenEntity> opt = refreshTokenRepository.findByTokenHash(hash);
        if (opt.isEmpty()) {
            throw unauthorized("Invalid refresh token");
        }
        RefreshTokenEntity entity = opt.get();
        if (entity.getExpiresAt().isBefore(Instant.now())) {
            refreshTokenRepository.delete(entity);
            throw unauthorized("Refresh token expired");
        }
        User user = entity.getUser();
        refreshTokenRepository.delete(entity);
        return issueTokens(user);
    }

    @Transactional
    public void revokeAllRefreshTokens(Long userId) {
        refreshTokenRepository.deleteAllForUserId(userId);
    }

    @Transactional
    public UserProfileDto profile(Long userId) {
        User user = userRepository.findByIdOptional(userId)
                .orElseThrow(() -> new WebApplicationException(Response.Status.NOT_FOUND));
        return toProfile(user);
    }

    private AuthTokens issueTokens(User user) {
        String access = jwtTokenService.createAccessToken(user.getId(), user.getEmail(), user.getFullName());
        String refreshPlain = TokenHasher.newOpaqueRefreshToken();
        String hash = TokenHasher.sha256Hex(refreshPlain);

        RefreshTokenEntity rt = new RefreshTokenEntity();
        rt.setUser(user);
        rt.setTokenHash(hash);
        rt.setExpiresAt(Instant.now().plus(Duration.ofDays(refreshTokenDays)));
        refreshTokenRepository.persist(rt);

        return new AuthTokens(toUserResponse(user), access, refreshPlain);
    }

    private static UserResponse toUserResponse(User user) {
        return new UserResponse(user.getId(), user.getFullName(), user.getEmail());
    }

    private static UserProfileDto toProfile(User user) {
        return new UserProfileDto(user.getId(), user.getFullName(), user.getEmail(), user.getUserType());
    }

    private static String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }

    private static WebApplicationException unauthorized(String message) {
        return new WebApplicationException(
                Response.status(Response.Status.UNAUTHORIZED)
                        .entity(Map.of("error", message))
                        .build());
    }

    private static WebApplicationException conflict(String message) {
        return new WebApplicationException(
                Response.status(Response.Status.CONFLICT)
                        .entity(Map.of("error", message))
                        .build());
    }

    public record UserResponse(Long id, String name, String email) {
    }

    public record UserProfileDto(Long id, String name, String email, String userType) {
    }

    public record AuthTokens(UserResponse user, String token, String refreshToken) {
    }
}
