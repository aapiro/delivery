package com.ilimitech.delivery.spring.restaurants;

import com.ilimitech.delivery.spring.restaurants.dto.CreateRestaurantDto;
import com.ilimitech.delivery.spring.restaurants.dto.RestaurantDto;
import com.ilimitech.delivery.spring.restaurants.dto.UpdateRestaurantDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class RestaurantMapper {

    public RestaurantDto toDto(Restaurant r){
        if (r == null) return null;
        RestaurantDto d = new RestaurantDto();
        d.setId(r.getId());
        d.setName(r.getName());
        d.setDescription(r.getDescription());
        d.setImageUrl(r.getImageUrl());
        d.setIsOpen(r.getIsOpen());
        d.setIsActive(r.getIsActive());
        d.setMinimumOrder(r.getMinimumOrder());
        d.setDeliveryFee(r.getDeliveryFee());
        d.setDeliveryTimeMin(r.getDeliveryTimeMin());
        d.setDeliveryTimeMax(r.getDeliveryTimeMax());
        d.setRating(r.getRating());
        d.setReviewCount(r.getReviewCount());
        d.setMaxActiveOrders(r.getMaxActiveOrders());
        d.setAvgPreparationTime(r.getAvgPreparationTime());
        d.setAvgDeliveryTime(r.getAvgDeliveryTime());
        d.setCancellationRate(r.getCancellationRate());
        d.setQualityScore(r.getQualityScore());
        d.setDeletedAt(r.getDeletedAt());
        d.setCreatedAt(r.getCreatedAt());
        d.setUpdatedAt(r.getUpdatedAt());
        return d;
    }

    public Restaurant toEntity(CreateRestaurantDto d){
        if (d == null) return null;
        Restaurant r = new Restaurant();
        r.setName(d.getName());
        r.setDescription(d.getDescription());
        r.setImageUrl(d.getImageUrl());
        r.setIsOpen(d.getIsOpen());
        r.setIsActive(d.getIsActive());
        r.setMinimumOrder(d.getMinimumOrder());
        r.setDeliveryFee(d.getDeliveryFee());
        r.setDeliveryTimeMin(d.getDeliveryTimeMin());
        r.setDeliveryTimeMax(d.getDeliveryTimeMax());
        r.setCreatedAt(LocalDateTime.now());
        r.setUpdatedAt(LocalDateTime.now());
        return r;
    }

    public Restaurant applyUpdate(Restaurant existing, UpdateRestaurantDto d){
        Optional.ofNullable(d.getName()).ifPresent(existing::setName);
        Optional.ofNullable(d.getDescription()).ifPresent(existing::setDescription);
        Optional.ofNullable(d.getImageUrl()).ifPresent(existing::setImageUrl);
        Optional.ofNullable(d.getIsOpen()).ifPresent(existing::setIsOpen);
        Optional.ofNullable(d.getIsActive()).ifPresent(existing::setIsActive);
        Optional.ofNullable(d.getMinimumOrder()).ifPresent(existing::setMinimumOrder);
        Optional.ofNullable(d.getDeliveryFee()).ifPresent(existing::setDeliveryFee);
        Optional.ofNullable(d.getDeliveryTimeMin()).ifPresent(existing::setDeliveryTimeMin);
        Optional.ofNullable(d.getDeliveryTimeMax()).ifPresent(existing::setDeliveryTimeMax);
        existing.setUpdatedAt(LocalDateTime.now());
        return existing;
    }
}

