package com.ilimitech.delivery.application.usecase;

import com.ilimitech.delivery.common.NotFoundException;
import com.ilimitech.delivery.application.port.out.RestaurantRepository;
import com.ilimitech.delivery.infrastructure.adapter.out.persistence.Category;
import com.ilimitech.delivery.infrastructure.adapter.out.persistence.MenuCategory;
import com.ilimitech.delivery.infrastructure.adapter.out.persistence.RestaurantEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class RestaurantService {

    @Inject
    RestaurantRepository restaurantRepository;

    public List<RestaurantEntity> getAllRestaurants() {
        return restaurantRepository.listAll();
    }

    public List<RestaurantEntity> getAllRestaurantsFiltered(String name, String cuisine, Boolean isOpen) {
        // Get all restaurants first
        List<RestaurantEntity> restaurantEntities = restaurantRepository.listAll();
        
        // Apply filters if provided
        if (name != null && !name.isBlank()) {
            restaurantEntities = restaurantEntities.stream()
                .filter(r -> r.getName() != null && r.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
        }

        if (cuisine != null && !cuisine.isBlank()) {
            restaurantEntities = restaurantEntities.stream()
                    .filter(r -> r.getCuisines() != null && r.getCuisines().stream()
                            .anyMatch(c -> c.name.equalsIgnoreCase(cuisine)))
                    .collect(Collectors.toList());
        }
        
        if (isOpen != null) {
            restaurantEntities = restaurantEntities.stream()
                .filter(r -> r.isOpen() == isOpen)
                .collect(Collectors.toList());
        }
        
        return restaurantEntities;
    }

    @Transactional
    public RestaurantEntity addRestaurant(RestaurantEntity restaurantEntity) {
        validateRestaurantData(restaurantEntity);
        restaurantRepository.persist(restaurantEntity);
        return restaurantEntity;
    }

    public RestaurantEntity getRestaurantById(Long id) {
        RestaurantEntity byId = restaurantRepository.findById(id);
        return byId;
    }

    @Transactional
    public RestaurantEntity updateRestaurant(Long id, RestaurantEntity updatedRestaurantEntity) {
        RestaurantEntity existing = restaurantRepository.findById(id);
        if (existing == null) {
            return null;
        }

        existing.setName(updatedRestaurantEntity.getName());
        existing.setCuisines(updatedRestaurantEntity.getCuisines());

        BigDecimal updatedRating = updatedRestaurantEntity.getRating();
        if (updatedRating != null) {
            if (updatedRating.compareTo(BigDecimal.ZERO) < 0 || updatedRating.compareTo(new BigDecimal("5.0")) > 0) {
                throw new IllegalArgumentException("Rating must be between 0 and 5");
            }
            existing.setRating(updatedRating.setScale(1, RoundingMode.HALF_UP));
        }

        String description = updatedRestaurantEntity.getDescription();
        if (description != null) {
            existing.setDescription(description);
        }

        Integer deliveryTimeMin = updatedRestaurantEntity.getDeliveryTimeMin();
        if (deliveryTimeMin != null) {
            existing.setDeliveryTimeMin(deliveryTimeMin);
        }

        Integer deliveryTimeMax = updatedRestaurantEntity.getDeliveryTimeMax();
        if (deliveryTimeMax != null) {
            existing.setDeliveryTimeMax(deliveryTimeMax);
        }

        BigDecimal deliveryFee = updatedRestaurantEntity.getDeliveryFee();
        if (deliveryFee != null) {
            existing.setDeliveryFee(deliveryFee);
        }

        BigDecimal minimumOrder = updatedRestaurantEntity.getMinimumOrder();
        if (minimumOrder != null) {
            existing.setMinimumOrder(minimumOrder);
        }

        boolean isOpen = updatedRestaurantEntity.isOpen();
        existing.setOpen(isOpen);

        String imageUrl = updatedRestaurantEntity.getImageUrl();
        if (imageUrl != null) {
            existing.setImageUrl(imageUrl);
        }
        return existing;
    }

    @Transactional
    public RestaurantEntity toggleRestaurantStatus(Long id) {
        RestaurantEntity restaurantEntity = getRestaurantById(id);
        restaurantEntity.setOpen(!restaurantEntity.isOpen());
        return restaurantEntity;
    }

    @Transactional
    public void deleteRestaurant(Long id) {
        RestaurantEntity restaurantEntity = restaurantRepository.findById(id);
        if (restaurantEntity == null) {
            throw new NotFoundException("Restaurant with ID " + id + " not found");
        }
        restaurantRepository.delete(restaurantEntity);
    }
    
    /**
     * Get all categories for restaurants
     */
    public List<MenuCategory> getAllCategories() {
        return restaurantRepository.listAll().stream()
                .flatMap(r -> r.categories.stream())
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Search restaurants by name or cuisine
     */
    public List<RestaurantEntity> searchRestaurants(String name, String cuisine) {
        List<RestaurantEntity> restaurantEntities = restaurantRepository.listAll();
        
        // Apply filters if provided
        if (name != null && !name.isEmpty()) {
            restaurantEntities = restaurantEntities.stream()
                .filter(r -> r.getName() != null && r.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
        }
        
        if (cuisine != null && !cuisine.isEmpty()) {
            restaurantEntities = restaurantEntities.stream()
                .filter(r -> r.getCuisines() != null && r.getCuisines().contains(cuisine.toLowerCase()))
                .collect(Collectors.toList());
        }
        
        return restaurantEntities;
    }

    private void validateRestaurantData(RestaurantEntity restaurant) {
        if (restaurant.getName() == null || restaurant.getName().isBlank()) {
            throw new IllegalArgumentException("Restaurant name is required");
        }

        BigDecimal rating = restaurant.getRating();
        if (rating != null && (rating.compareTo(BigDecimal.ZERO) < 0 || rating.compareTo(new BigDecimal("5.0")) > 0)) {
            throw new IllegalArgumentException("Rating must be between 0 and 5");
        }

        if (restaurant.getDeliveryTimeMin() != null && restaurant.getDeliveryTimeMax() != null
                && restaurant.getDeliveryTimeMin() > restaurant.getDeliveryTimeMax()) {
            throw new IllegalArgumentException("Minimum delivery time cannot be greater than maximum");
        }

        if (restaurant.getDeliveryFee() != null && restaurant.getDeliveryFee().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Delivery fee cannot be negative");
        }
    }
}