package com.ilimitech.delivery.spring.dishavailability;

import com.ilimitech.delivery.spring.dishavailability.dto.CreateDishAvailabilityDto;
import com.ilimitech.delivery.spring.dishavailability.dto.DishAvailabilityDto;

import java.util.List;

public interface DishAvailabilityService {
    List<DishAvailabilityDto> findAll();
    DishAvailabilityDto create(CreateDishAvailabilityDto dto);
    void delete(Long id);
}

