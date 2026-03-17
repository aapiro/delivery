package com.ilimitech.delivery.spring.dishes;

import com.ilimitech.delivery.spring.dishes.dto.CreateDishDto;
import com.ilimitech.delivery.spring.dishes.dto.DishDto;
import com.ilimitech.delivery.spring.dishes.dto.UpdateDishDto;

import java.util.List;

public interface DishService {
    List<DishDto> findAll();
    DishDto findById(Long id);
    DishDto create(CreateDishDto dto);
    DishDto update(Long id, UpdateDishDto dto);
    void delete(Long id);
}

