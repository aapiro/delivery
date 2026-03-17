package com.ilimitech.delivery.spring.dishes;

import com.ilimitech.delivery.spring.dishes.dto.CreateDishDto;
import com.ilimitech.delivery.spring.dishes.dto.DishDto;
import com.ilimitech.delivery.spring.dishes.dto.UpdateDishDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class DishMapper {

    public DishDto toDto(Dish d){
        if (d == null) return null;
        DishDto dto = new DishDto();
        dto.setId(d.getId());
        dto.setRestaurantId(d.getRestaurantId());
        dto.setCategoryId(d.getCategoryId());
        dto.setName(d.getName());
        dto.setDescription(d.getDescription());
        dto.setPrice(d.getPrice());
        dto.setImageUrl(d.getImageUrl());
        dto.setIsAvailable(d.getIsAvailable());
        dto.setIsPopular(d.getIsPopular());
        dto.setPreparationTime(d.getPreparationTime());
        dto.setStock(d.getStock());
        dto.setStockUnlimited(d.getStockUnlimited());
        dto.setPopularityScore(d.getPopularityScore());
        dto.setDeletedAt(d.getDeletedAt());
        dto.setCreatedAt(d.getCreatedAt());
        dto.setUpdatedAt(d.getUpdatedAt());
        return dto;
    }

    public Dish toEntity(CreateDishDto d){
        if (d == null) return null;
        Dish e = new Dish();
        e.setRestaurantId(d.getRestaurantId());
        e.setCategoryId(d.getCategoryId());
        e.setName(d.getName());
        e.setDescription(d.getDescription());
        e.setPrice(d.getPrice());
        e.setImageUrl(d.getImageUrl());
        e.setIsAvailable(d.getIsAvailable());
        e.setIsPopular(d.getIsPopular());
        e.setPreparationTime(d.getPreparationTime());
        e.setStock(d.getStock());
        e.setStockUnlimited(d.getStockUnlimited());
        e.setCreatedAt(LocalDateTime.now());
        e.setUpdatedAt(LocalDateTime.now());
        return e;
    }

    public Dish applyUpdate(Dish existing, UpdateDishDto d){
        Optional.ofNullable(d.getRestaurantId()).ifPresent(existing::setRestaurantId);
        Optional.ofNullable(d.getCategoryId()).ifPresent(existing::setCategoryId);
        Optional.ofNullable(d.getName()).ifPresent(existing::setName);
        Optional.ofNullable(d.getDescription()).ifPresent(existing::setDescription);
        Optional.ofNullable(d.getPrice()).ifPresent(existing::setPrice);
        Optional.ofNullable(d.getImageUrl()).ifPresent(existing::setImageUrl);
        Optional.ofNullable(d.getIsAvailable()).ifPresent(existing::setIsAvailable);
        Optional.ofNullable(d.getIsPopular()).ifPresent(existing::setIsPopular);
        Optional.ofNullable(d.getPreparationTime()).ifPresent(existing::setPreparationTime);
        Optional.ofNullable(d.getStock()).ifPresent(existing::setStock);
        Optional.ofNullable(d.getStockUnlimited()).ifPresent(existing::setStockUnlimited);
        existing.setUpdatedAt(LocalDateTime.now());
        return existing;
    }
}

