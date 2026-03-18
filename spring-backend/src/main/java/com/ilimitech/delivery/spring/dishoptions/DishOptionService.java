package com.ilimitech.delivery.spring.dishoptions;

import com.ilimitech.delivery.spring.dishoptions.dto.CreateDishOptionDto;
import com.ilimitech.delivery.spring.dishoptions.dto.DishOptionDto;

import java.util.List;

public interface DishOptionService {
    List<DishOptionDto> findAll();
    DishOptionDto create(CreateDishOptionDto dto);
    void delete(Long id);
}

