package com.ilimitech.delivery.spring.dishoptionvalues;

import com.ilimitech.delivery.spring.dishoptionvalues.dto.CreateDishOptionValueDto;
import com.ilimitech.delivery.spring.dishoptionvalues.dto.DishOptionValueDto;

import java.util.List;

public interface DishOptionValueService {
    List<DishOptionValueDto> findAll();
    DishOptionValueDto create(CreateDishOptionValueDto dto);
    void delete(Long id);
}

