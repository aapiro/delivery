package com.ilimitech.delivery.application.port.out;

import com.ilimitech.delivery.infrastructure.adapter.out.persistence.CartItemEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CartItemRepository implements PanacheRepository<CartItemEntity> {

    public List<CartItemEntity> findByUserId(Long userId) {
        return list("userId", userId);
    }

    public Optional<CartItemEntity> findByUserIdAndDishId(Long userId, Long dishId) {
        return find("userId = ?1 and dish.id = ?2", userId, dishId).firstResultOptional();
    }

    public Optional<CartItemEntity> findByUserIdAndItemId(Long userId, Long itemId) {
        return find("userId = ?1 and id = ?2", userId, itemId).firstResultOptional();
    }

    public void deleteByUserId(Long userId) {
        delete("userId", userId);
    }
}

