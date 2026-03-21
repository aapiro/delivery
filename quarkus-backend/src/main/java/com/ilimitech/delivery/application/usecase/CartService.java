package com.ilimitech.delivery.application.usecase;

import com.ilimitech.delivery.application.port.out.CartItemRepository;
import com.ilimitech.delivery.application.port.out.DishRepository;
import com.ilimitech.delivery.application.port.out.UserRepository;
import com.ilimitech.delivery.infrastructure.adapter.out.persistence.CartItemEntity;
import com.ilimitech.delivery.infrastructure.adapter.out.persistence.DishEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class CartService {

    @Inject
    CartItemRepository cartItemRepository;

    @Inject
    DishRepository dishRepository;

    @Inject
    UserRepository userRepository;

    public CartSnapshot getCart(Long userId) {
        ensureUserExists(userId);
        List<CartItemEntity> items = cartItemRepository.findByUserId(userId);
        return toSnapshot(items);
    }

    @Transactional
    public CartSnapshot add(Long userId, Long dishId, Integer quantity, String specialInstructions) {
        ensureUserExists(userId);
        if (quantity == null || quantity <= 0) {
            throw badRequest("quantity must be > 0");
        }

        DishEntity dish = dishRepository.findByIdOptional(dishId)
                .orElseThrow(() -> notFound("Dish not found with ID: " + dishId));

        CartItemEntity entity = cartItemRepository.findByUserIdAndDishId(userId, dishId).orElse(null);
        if (entity == null) {
            entity = new CartItemEntity();
            entity.setUserId(userId);
            entity.setDish(dish);
            entity.setQuantity(quantity);
            entity.setSpecialInstructions(specialInstructions);
            cartItemRepository.persist(entity);
        } else {
            entity.setQuantity(entity.getQuantity() + quantity);
            if (specialInstructions != null) {
                entity.setSpecialInstructions(specialInstructions);
            }
        }

        return toSnapshot(cartItemRepository.findByUserId(userId));
    }

    @Transactional
    public CartSnapshot update(Long userId, Long itemId, Integer quantity) {
        ensureUserExists(userId);
        if (quantity == null || quantity <= 0) {
            throw badRequest("quantity must be > 0");
        }
        CartItemEntity entity = cartItemRepository.findByUserIdAndItemId(userId, itemId)
                .orElseThrow(() -> notFound("Cart item not found"));

        entity.setQuantity(quantity);
        return toSnapshot(cartItemRepository.findByUserId(userId));
    }

    @Transactional
    public CartSnapshot remove(Long userId, Long itemId) {
        ensureUserExists(userId);
        CartItemEntity entity = cartItemRepository.findByUserIdAndItemId(userId, itemId)
                .orElseThrow(() -> notFound("Cart item not found"));
        cartItemRepository.delete(entity);
        return toSnapshot(cartItemRepository.findByUserId(userId));
    }

    @Transactional
    public CartSnapshot clear(Long userId) {
        ensureUserExists(userId);
        cartItemRepository.deleteByUserId(userId);
        return toSnapshot(List.of());
    }

    private void ensureUserExists(Long userId) {
        if (userId == null || userId <= 0) {
            throw badRequest("userId is required");
        }
        if (userRepository.findByIdOptional(userId).isEmpty()) {
            throw notFound("User not found with ID: " + userId);
        }
    }

    private static CartSnapshot toSnapshot(List<CartItemEntity> entities) {
        List<CartItemView> items = entities.stream().map(CartService::toView).toList();
        BigDecimal total = items.stream()
                .map(CartItemView::totalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Long restaurantId = items.stream().findFirst().map(i -> i.dish().restaurantId()).orElse(null);
        return new CartSnapshot(items, total, restaurantId);
    }

    private static CartItemView toView(CartItemEntity entity) {
        DishEntity d = entity.getDish();
        BigDecimal unit = d.getPrice() != null ? d.getPrice() : BigDecimal.ZERO;
        BigDecimal total = unit.multiply(BigDecimal.valueOf(entity.getQuantity()));

        RestaurantView restaurant = new RestaurantView(
                d.getRestaurant().getId(),
                d.getRestaurant().getName(),
                d.getRestaurant().getDeliveryFee(),
                d.getRestaurant().getDeliveryTimeMin(),
                d.getRestaurant().getDeliveryTimeMax(),
                d.getRestaurant().getIsOpen()
        );

        DishView dish = new DishView(
                d.getId(),
                d.getName(),
                d.getDescription(),
                unit,
                d.getImageUrl(),
                d.isAvailable(),
                d.getRestaurant().getId(),
                restaurant
        );

        return new CartItemView(
                entity.getId(),
                entity.getUserId(),
                d.getId(),
                entity.getQuantity(),
                unit,
                total,
                entity.getSpecialInstructions(),
                dish
        );
    }

    private static WebApplicationException badRequest(String message) {
        return new WebApplicationException(
                Response.status(Response.Status.BAD_REQUEST).entity(Map.of("error", message)).build());
    }

    private static WebApplicationException notFound(String message) {
        return new WebApplicationException(
                Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", message)).build());
    }

    public record CartSnapshot(List<CartItemView> items, BigDecimal total, Long restaurantId) {
    }

    public record CartItemView(
            Long id,
            Long userId,
            Long dishId,
            Integer quantity,
            BigDecimal unitPrice,
            BigDecimal totalPrice,
            String specialInstructions,
            DishView dish
    ) {
    }

    public record DishView(
            Long id,
            String name,
            String description,
            BigDecimal price,
            String imageUrl,
            boolean isAvailable,
            Long restaurantId,
            RestaurantView restaurant
    ) {
    }

    public record RestaurantView(
            Long id,
            String name,
            BigDecimal deliveryFee,
            Integer deliveryTimeMin,
            Integer deliveryTimeMax,
            Boolean isOpen
    ) {
    }
}

