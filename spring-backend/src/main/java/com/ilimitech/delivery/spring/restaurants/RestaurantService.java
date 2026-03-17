package com.ilimitech.delivery.spring.restaurants;

import com.ilimitech.delivery.spring.restaurants.dto.CreateRestaurantDto;
import com.ilimitech.delivery.spring.restaurants.dto.RestaurantDto;
import com.ilimitech.delivery.spring.restaurants.dto.UpdateRestaurantDto;

import java.util.List;

public interface RestaurantService {
    List<RestaurantDto> findAll();
    RestaurantDto findById(Long id);
    RestaurantDto create(CreateRestaurantDto dto);
    RestaurantDto update(Long id, UpdateRestaurantDto dto);
    void delete(Long id);
}

