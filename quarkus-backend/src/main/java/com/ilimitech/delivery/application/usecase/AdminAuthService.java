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
import java.util.*;

/**
 * Autenticación del panel admin: mismos usuarios {@link User} con {@code user_type} administrativo.
 */
@ApplicationScoped
public class AdminAuthService {

    static final Set<String> ADMIN_USER_TYPES = Set.of(
            "SUPER_ADMIN",
            "ADMIN",
            "MANAGER",
            "MODERATOR"
    );

    @Inject
    UserRepository userRepository;

    @Inject
    RefreshTokenRepository refreshTokenRepository;

    @Inject
    JwtTokenService jwtTokenService;

    @ConfigProperty(name = "delivery.auth.access-token-ttl-minutes", defaultValue = "15")
    long accessTokenTtlMinutes;

    @ConfigProperty(name = "delivery.auth.refresh-token-days", defaultValue = "30")
    long refreshTokenDays;

    public static boolean isAdminUserType(String userType) {
        if (userType == null) {
            return false;
        }
        return ADMIN_USER_TYPES.contains(userType.trim().toUpperCase(Locale.ROOT));
    }

    @Transactional
    public AdminAuthTokens login(String email, String password) {
        String norm = normalizeEmail(email);
        User user = userRepository.findByEmail(norm)
                .orElseThrow(() -> unauthorized("Invalid credentials"));
        if (Boolean.FALSE.equals(user.getIsActive())) {
            throw unauthorized("Invalid credentials");
        }
        if (!isAdminUserType(user.getUserType())) {
            throw unauthorized("Invalid credentials");
        }
        if (user.getPasswordHash() == null
                || !BCrypt.verifyer().verify(password.toCharArray(), user.getPasswordHash()).verified) {
            throw unauthorized("Invalid credentials");
        }
        return issueAdminTokens(user);
    }

    @Transactional
    public AdminAuthTokens refresh(String refreshTokenPlain) {
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
        if (!isAdminUserType(user.getUserType()) || Boolean.FALSE.equals(user.getIsActive())) {
            refreshTokenRepository.delete(entity);
            throw unauthorized("Invalid refresh token");
        }
        refreshTokenRepository.delete(entity);
        return issueAdminTokens(user);
    }

    @Transactional
    public AdminProfileDto profile(Long userId) {
        User user = userRepository.findByIdOptional(userId)
                .orElseThrow(() -> new WebApplicationException(Response.Status.NOT_FOUND));
        if (!isAdminUserType(user.getUserType())) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return toAdminProfile(user);
    }

    @Transactional
    public AdminProfileDto updateProfile(Long userId, String newName, String newEmail) {
        User user = userRepository.findByIdOptional(userId)
                .orElseThrow(() -> new WebApplicationException(Response.Status.NOT_FOUND));
        if (!isAdminUserType(user.getUserType())) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        if (newName != null && !newName.isBlank()) {
            user.setFullName(newName.trim());
        }
        if (newEmail != null && !newEmail.isBlank()) {
            String norm = normalizeEmail(newEmail);
            userRepository.findByEmail(norm)
                    .filter(u -> !u.getId().equals(userId))
                    .ifPresent(u -> {
                        throw conflict("Email already in use");
                    });
            user.setEmail(norm);
        }
        return toAdminProfile(user);
    }

    @Transactional
    public void revokeAllRefreshTokens(Long userId) {
        refreshTokenRepository.deleteAllForUserId(userId);
    }

    private AdminAuthTokens issueAdminTokens(User user) {
        String role = user.getUserType() != null ? user.getUserType().toUpperCase(Locale.ROOT) : "ADMIN";
        String access = jwtTokenService.createAdminAccessToken(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                role
        );
        String refreshPlain = TokenHasher.newOpaqueRefreshToken();
        String refreshHash = TokenHasher.sha256Hex(refreshPlain);

        RefreshTokenEntity rt = new RefreshTokenEntity();
        rt.setUser(user);
        rt.setTokenHash(refreshHash);
        rt.setExpiresAt(Instant.now().plus(Duration.ofDays(refreshTokenDays)));
        refreshTokenRepository.persist(rt);

        return new AdminAuthTokens(toAdminProfile(user), access, refreshPlain, accessTokenTtlMinutes * 60);
    }

    private AdminProfileDto toAdminProfile(User user) {
        String role = user.getUserType() != null ? user.getUserType().toUpperCase(Locale.ROOT) : "ADMIN";
        List<String> permissions = AdminPermissionCatalog.permissionsForRole(role);
        String created = user.getCreatedAt() != null ? user.getCreatedAt().toString() : Instant.now().toString();
        return new AdminProfileDto(
                user.getId(),
                user.getEmail(),
                user.getFullName() != null ? user.getFullName() : "",
                role,
                permissions,
                Boolean.TRUE.equals(user.getIsActive()),
                created,
                created
        );
    }

    private static String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
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

    public record AdminProfileDto(
            Long id,
            String email,
            String name,
            String role,
            List<String> permissions,
            boolean isActive,
            String createdAt,
            String updatedAt
    ) {
    }

    public record AdminAuthTokens(AdminProfileDto admin, String token, String refreshToken, long expiresIn) {
    }
}
