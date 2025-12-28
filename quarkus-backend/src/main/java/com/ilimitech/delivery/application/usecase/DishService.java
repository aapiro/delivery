package com.ilimitech.delivery.application.usecase;

import com.ilimitech.delivery.common.NotFoundException;
import com.ilimitech.delivery.application.port.out.DishRepository;
import com.ilimitech.delivery.infrastructure.adapter.out.persistence.Category;
import com.ilimitech.delivery.infrastructure.adapter.out.persistence.DishEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped
public class DishService {

    @Inject
    DishRepository dishRepository;

    public List<DishEntity> getAllDishes() {
        return dishRepository.listAll();
    }

    public List<DishEntity> getAllDishesFiltered(String name, Long categoryId, Boolean isAvailable) {
        // Get all dishes first
        List<DishEntity> dishEntities = dishRepository.listAll();
        
        // Apply filters if provided
        if (name != null && !name.isEmpty()) {
            dishEntities = dishEntities.stream()
                .filter(d -> d.getName() != null && d.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
        }
        
        if (categoryId != null) {
            dishEntities = dishEntities.stream()
                .filter(d -> d.getCategory() != null && Objects.equals(d.getCategory().getId(), categoryId))
                .collect(Collectors.toList());
        }
        
        if (isAvailable != null) {
            dishEntities = dishEntities.stream()
                .filter(d -> d.isAvailable() == isAvailable)
                .collect(Collectors.toList());
        }
        
        return dishEntities;
    }

    @Transactional
    public DishEntity addDish(DishEntity dishEntity) {
        dishRepository.persist(dishEntity);
        return dishEntity;
    }

    public DishEntity getDishById(Long id) {
        return dishRepository.findById(id);
    }

    @Transactional
    public DishEntity updateDish(Long id, DishEntity updatedDishEntity) {
        DishEntity existingDishEntity = dishRepository.findById(id);

        if (existingDishEntity == null) {
            throw new NotFoundException("Dish not found");
        }

        // Update fields from the updatedDish
        existingDishEntity.setName(updatedDishEntity.getName());
        existingDishEntity.setDescription(updatedDishEntity.getDescription());
        existingDishEntity.setPrice(updatedDishEntity.getPrice());
        existingDishEntity.setImageUrl(updatedDishEntity.getImageUrl());
        existingDishEntity.setRestaurant(updatedDishEntity.getRestaurant());
        existingDishEntity.setCategory(updatedDishEntity.getCategory());
        existingDishEntity.setAvailable(updatedDishEntity.isAvailable());

        return existingDishEntity;
    }

    @Transactional
    public DishEntity toggleDishAvailability(Long id) {
        DishEntity dishEntity = dishRepository.findById(id);
        
        if (dishEntity == null) {
            throw new NotFoundException("Dish not found");
        }
        
        // Toggle the availability status
        Boolean currentStatus = dishEntity.isAvailable();
        if (currentStatus == null) {
            currentStatus = false;
        }
        dishEntity.setAvailable(!currentStatus);
        
        return dishEntity;
    }

    @Transactional
    public void deleteDish(Long id) {
        DishEntity dishEntity = dishRepository.findById(id);

        if (dishEntity == null) {
            throw new NotFoundException("Dish not found");
        }

        dishRepository.delete(dishEntity);
    }
    
    /**
     * Search dishes by name or category
     */
    public List<DishEntity> searchDishes(String name, Long categoryId) {
        List<DishEntity> dishEntities = dishRepository.listAll();
        
        // Apply filters if provided
        if (name != null && !name.isEmpty()) {
            dishEntities = dishEntities.stream()
                .filter(d -> d.getName() != null && d.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
        }
        
        if (categoryId != null) {
            dishEntities = dishEntities.stream()
                .filter(d -> d.getCategory() != null && d.getCategory().getId() == categoryId)
                .collect(Collectors.toList());
        }
        
        return dishEntities;
    }

    /**
     * Get dish categories for a specific restaurant
     */
    public List<Category> getRestaurantDishCategories(Long restaurantId) {
        // This would typically query the database to find all categories associated with dishes of this restaurant
        // For now, we'll just return an empty list or implement later if needed
        return List.of();
    }
}