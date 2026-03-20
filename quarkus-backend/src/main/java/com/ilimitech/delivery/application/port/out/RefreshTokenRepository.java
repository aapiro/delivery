package com.ilimitech.delivery.application.port.out;

import com.ilimitech.delivery.infrastructure.adapter.out.persistence.RefreshTokenEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class RefreshTokenRepository implements PanacheRepository<RefreshTokenEntity> {

    public Optional<RefreshTokenEntity> findByTokenHash(String tokenHash) {
        return find("tokenHash", tokenHash).firstResultOptional();
    }

    public void deleteAllForUserId(Long userId) {
        list("user.id", userId).forEach(this::delete);
    }
}
