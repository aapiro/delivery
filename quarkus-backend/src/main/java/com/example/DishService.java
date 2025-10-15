package com.example;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class DishService {

    @Inject
    DishRepository dishRepository;

    public List<Dish> getAllDishes() {
        return dishRepository.listAll();
    }

    public List<Dish> getAllDishesFiltered(String name, Long categoryId, Boolean isAvailable) {
        // Get all dishes first
        List<Dish> dishes = dishRepository.listAll();
        
        // Apply filters if provided
        if (name != null && !name.isEmpty()) {
            dishes = dishes.stream()
                .filter(d -> d.getName() != null && d.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
        }
        
        if (categoryId != null) {
            dishes = dishes.stream()
                .filter(d -> d.getCategory() != null && d.getCategory().getId() == categoryId)
                .collect(Collectors.toList());
        }
        
        if (isAvailable != null) {
            dishes = dishes.stream()
                .filter(d -> d.isAvailable() == isAvailable)
                .collect(Collectors.toList());
        }
        
        return dishes;
    }

    @Transactional
    public Dish addDish(Dish dish) {
        dishRepository.persist(dish);
        return dish;
    }

    public Dish getDishById(Long id) {
        return dishRepository.findById(id);
    }

    @Transactional
    public Dish updateDish(Long id, Dish updatedDish) {
        Dish existingDish = dishRepository.findById(id);

        if (existingDish == null) {
            throw new NotFoundException("Dish not found");
        }

        // Update fields from the updatedDish
        existingDish.setName(updatedDish.getName());
        existingDish.setDescription(updatedDish.getDescription());
        existingDish.setPrice(updatedDish.getPrice());
        existingDish.setImageUrl(updatedDish.getImageUrl());
        existingDish.setRestaurant(updatedDish.getRestaurant());
        existingDish.setCategory(updatedDish.getCategory());
        existingDish.setAvailable(updatedDish.isAvailable());

        return existingDish;
    }

    @Transactional
    public Dish toggleDishAvailability(Long id) {
        Dish dish = dishRepository.findById(id);
        
        if (dish == null) {
            throw new NotFoundException("Dish not found");
        }
        
        // Toggle the availability status
        Boolean currentStatus = dish.isAvailable();
        if (currentStatus == null) {
            currentStatus = false;
        }
        dish.setAvailable(!currentStatus);
        
        return dish;
    }

    @Transactional
    public void deleteDish(Long id) {
        Dish dish = dishRepository.findById(id);

        if (dish == null) {
            throw new NotFoundException("Dish not found");
        }

        dishRepository.delete(dish);
    }
    
    /**
     * Search dishes by name or category
     */
    public List<Dish> searchDishes(String name, Long categoryId) {
        List<Dish> dishes = dishRepository.listAll();
        
        // Apply filters if provided
        if (name != null && !name.isEmpty()) {
            dishes = dishes.stream()
                .filter(d -> d.getName() != null && d.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
        }
        
        if (categoryId != null) {
            dishes = dishes.stream()
                .filter(d -> d.getCategory() != null && d.getCategory().getId() == categoryId)
                .collect(Collectors.toList());
        }
        
        return dishes;
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