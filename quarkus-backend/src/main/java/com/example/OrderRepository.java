package com.example;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class OrderRepository implements PanacheRepository<Order> {

    public Optional<Order> findByIdOptional(Long id) {
        return find("id", id).firstResultOptional();
    }

    public List<Order> findByUserId(Long userId) {
        return list("userId", userId);
    }

    public List<Order> findByUserIdAndStatus(Long userId, Order.OrderStatus status) {
        return list("userId = ?1 AND status = ?2", userId, status);
    }
}