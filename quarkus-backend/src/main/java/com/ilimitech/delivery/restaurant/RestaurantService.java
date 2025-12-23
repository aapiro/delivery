package com.ilimitech.delivery.restaurant;

import com.ilimitech.delivery.NotFoundException;
import com.ilimitech.delivery.category.Category;
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

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.listAll();
    }

    public List<Restaurant> getAllRestaurantsFiltered(String name, String cuisine, Boolean isOpen) {
        // Get all restaurants first
        List<Restaurant> restaurants = restaurantRepository.listAll();
        
        // Apply filters if provided
        if (name != null && !name.isEmpty()) {
            restaurants = restaurants.stream()
                .filter(r -> r.getName() != null && r.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
        }
        
        if (cuisine != null && !cuisine.isEmpty()) {
            restaurants = restaurants.stream()
                .filter(r -> r.getCuisine() != null && r.getCuisine().toLowerCase().contains(cuisine.toLowerCase()))
                .collect(Collectors.toList());
        }
        
        if (isOpen != null) {
            restaurants = restaurants.stream()
                .filter(r -> r.isOpen() == isOpen)
                .collect(Collectors.toList());
        }
        
        return restaurants;
    }

    @Transactional
    public Restaurant addRestaurant(Restaurant restaurant) {
        restaurantRepository.persist(restaurant);
        return restaurant;
    }

    public Restaurant getRestaurantById(Long id) {
        return restaurantRepository.findById(id);
    }

    @Transactional
    public Restaurant updateRestaurant(Long id, Restaurant updatedRestaurant) {
        Restaurant existing = restaurantRepository.findById(id);
        if (existing == null) {
            throw new NotFoundException("Restaurant with ID " + id + " not found");
        }

        // Update fields
        existing.setName(updatedRestaurant.getName());
        existing.setCuisine(updatedRestaurant.getCuisine());

        // Handle rating update if provided
        BigDecimal updatedRating = updatedRestaurant.getRating();
        if (updatedRating != null) {
            // Validate rating is between 0 and 5
            if (updatedRating.compareTo(BigDecimal.ZERO) < 0 || updatedRating.compareTo(new BigDecimal("5.0")) > 0) {
                throw new IllegalArgumentException("Rating must be between 0 and 5");
            }
            // Round to 1 decimal place
            existing.setRating(updatedRating.setScale(1, RoundingMode.HALF_UP));
        }

        // Update other fields if provided
        String description = updatedRestaurant.getDescription();
        if (description != null) {
            existing.setDescription(description);
        }

        Integer deliveryTimeMin = updatedRestaurant.getDeliveryTimeMin();
        if (deliveryTimeMin != null) {
            existing.setDeliveryTimeMin(deliveryTimeMin);
        }

        Integer deliveryTimeMax = updatedRestaurant.getDeliveryTimeMax();
        if (deliveryTimeMax != null) {
            existing.setDeliveryTimeMax(deliveryTimeMax);
        }

        BigDecimal deliveryFee = updatedRestaurant.getDeliveryFee();
        if (deliveryFee != null) {
            existing.setDeliveryFee(deliveryFee);
        }

        BigDecimal minimumOrder = updatedRestaurant.getMinimumOrder();
        if (minimumOrder != null) {
            existing.setMinimumOrder(minimumOrder);
        }

        Boolean isOpen = updatedRestaurant.isOpen();
        if (isOpen != null) {
            existing.setOpen(isOpen);
        }

        String imageUrl = updatedRestaurant.getImageUrl();
        if (imageUrl != null) {
            existing.setImageUrl(imageUrl);
        }

        return existing;
    }

    @Transactional
    public Restaurant toggleRestaurantStatus(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id);
        if (restaurant == null) {
            throw new NotFoundException("Restaurant with ID " + id + " not found");
        }
        
        // Toggle the open status
        Boolean currentStatus = restaurant.isOpen();
        if (currentStatus == null) {
            currentStatus = false;
        }
        restaurant.setOpen(!currentStatus);
        
        return restaurant;
    }

    @Transactional
    public void deleteRestaurant(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id);
        if (restaurant != null) {
            restaurantRepository.delete(restaurant);
        } else {
            throw new NotFoundException("Restaurant with ID " + id + " not found");
        }
    }
    
    /**
     * Get all categories for restaurants
     */
    public List<Category> getAllCategories() {
        // This would typically return a list of categories from the database
        // For now, we'll just return an empty list or implement later if needed
        return List.of();
    }

    /**
     * Search restaurants by name or cuisine
     */
    public List<Restaurant> searchRestaurants(String name, String cuisine) {
        List<Restaurant> restaurants = restaurantRepository.listAll();
        
        // Apply filters if provided
        if (name != null && !name.isEmpty()) {
            restaurants = restaurants.stream()
                .filter(r -> r.getName() != null && r.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
        }
        
        if (cuisine != null && !cuisine.isEmpty()) {
            restaurants = restaurants.stream()
                .filter(r -> r.getCuisine() != null && r.getCuisine().toLowerCase().contains(cuisine.toLowerCase()))
                .collect(Collectors.toList());
        }
        
        return restaurants;
    }
}