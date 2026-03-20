package com.ilimitech.delivery.application.port.out;

import com.ilimitech.delivery.infrastructure.adapter.out.persistence.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

    public Optional<User> findByEmail(String email) {
        if (email == null || email.isBlank()) {
            return Optional.empty();
        }
        String normalized = email.trim().toLowerCase();
        return find("email", normalized).firstResultOptional();
    }
}