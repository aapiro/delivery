package com.ilimitech.delivery.application.usecase;

import com.ilimitech.delivery.common.NotFoundException;
import com.ilimitech.delivery.application.port.out.DishRepository;
import com.ilimitech.delivery.application.port.out.RestaurantRepository;
import com.ilimitech.delivery.infrastructure.adapter.in.rest.dto.AdminDishWriteDto;
import com.ilimitech.delivery.infrastructure.adapter.out.persistence.Category;
import com.ilimitech.delivery.infrastructure.adapter.out.persistence.DishEntity;
import com.ilimitech.delivery.infrastructure.adapter.out.persistence.MenuCategory;
import com.ilimitech.delivery.infrastructure.adapter.out.persistence.RestaurantEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped
public class DishService {

    @Inject
    DishRepository dishRepository;

    @Inject
    RestaurantRepository restaurantRepository;

    public List<DishEntity> getAllDishes() {
        return dishRepository.listAll();
    }

    public List<DishEntity> getAllDishesFiltered(
            String name,
            Long categoryId,
            Boolean isAvailable,
            Long restaurantId) {
        List<DishEntity> dishEntities = dishRepository.listAll();

        if (name != null && !name.isEmpty()) {
            dishEntities = dishEntities.stream()
                    .filter(d -> d.getName() != null && d.getName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (restaurantId != null) {
            dishEntities = dishEntities.stream()
                    .filter(d -> d.getRestaurant() != null && Objects.equals(d.getRestaurant().getId(), restaurantId))
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

    @Transactional
    public DishEntity addDishFromDto(AdminDishWriteDto dto) {
        if (dto == null || dto.restaurantId == null) {
            throw new IllegalArgumentException("restaurantId is required");
        }
        RestaurantEntity restaurant = restaurantRepository.findById(dto.restaurantId);
        if (restaurant == null) {
            throw new NotFoundException("Restaurant not found");
        }
        DishEntity e = new DishEntity();
        e.setName(Objects.requireNonNull(dto.name, "name is required"));
        e.setDescription(dto.description != null ? dto.description : "");
        e.setPrice(dto.price != null ? dto.price : BigDecimal.ZERO);
        e.setImageUrl(dto.imageUrl != null ? dto.imageUrl : "");
        e.setAvailable(dto.isAvailable != null ? dto.isAvailable : true);
        e.setRestaurant(restaurant);
        if (dto.categoryId != null) {
            MenuCategory cat = MenuCategory.findById(dto.categoryId);
            e.setCategory(cat);
        }
        dishRepository.persist(e);
        return e;
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
    public DishEntity updateDishFromDto(Long id, AdminDishWriteDto dto) {
        DishEntity existing = dishRepository.findById(id);
        if (existing == null) {
            throw new NotFoundException("Dish not found");
        }
        if (dto.name != null) {
            existing.setName(dto.name);
        }
        if (dto.description != null) {
            existing.setDescription(dto.description);
        }
        if (dto.price != null) {
            existing.setPrice(dto.price);
        }
        if (dto.imageUrl != null) {
            existing.setImageUrl(dto.imageUrl);
        }
        if (dto.isAvailable != null) {
            existing.setAvailable(dto.isAvailable);
        }
        if (dto.restaurantId != null) {
            RestaurantEntity restaurant = restaurantRepository.findById(dto.restaurantId);
            if (restaurant == null) {
                throw new NotFoundException("Restaurant not found");
            }
            existing.setRestaurant(restaurant);
        }
        if (dto.categoryId != null) {
            MenuCategory cat = MenuCategory.findById(dto.categoryId);
            existing.setCategory(cat);
        }
        return existing;
    }

    @Transactional
    public DishEntity toggleDishAvailability(Long id) {
        DishEntity dishEntity = dishRepository.findById(id);

        if (dishEntity == null) {
            throw new NotFoundException("Dish not found");
        }

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

    public List<DishEntity> searchDishes(String name, Long categoryId) {
        List<DishEntity> dishEntities = dishRepository.listAll();

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
     * Categorías de menú ({@link MenuCategory}) asociadas a un restaurante.
     */
    public List<MenuCategory> getMenuCategoriesForRestaurant(Long restaurantId) {
        if (restaurantId == null) {
            return List.of();
        }
        return MenuCategory.list("restaurant.id = ?1", restaurantId);
    }

    /**
     * @deprecated Usar {@link #getMenuCategoriesForRestaurant(Long)}; se mantiene por compatibilidad con firmas antiguas.
     */
    @Deprecated
    public List<Category> getRestaurantDishCategories(Long restaurantId) {
        return List.of();
    }
}
