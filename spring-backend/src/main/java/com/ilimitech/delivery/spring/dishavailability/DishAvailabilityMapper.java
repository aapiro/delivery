package com.ilimitech.delivery.spring.dishavailability;

import com.ilimitech.delivery.spring.dishavailability.dto.CreateDishAvailabilityDto;
import com.ilimitech.delivery.spring.dishavailability.dto.DishAvailabilityDto;
import org.springframework.stereotype.Component;

@Component
public class DishAvailabilityMapper {

    public DishAvailabilityDto toDto(DishAvailability e) {
        if (e == null) return null;
        DishAvailabilityDto dto = new DishAvailabilityDto();
        dto.setId(e.getId());
        dto.setDishId(e.getDishId());
        dto.setDayOfWeek(e.getDayOfWeek());
        dto.setStartTime(e.getStartTime());
        dto.setEndTime(e.getEndTime());
        return dto;
    }

    public DishAvailability toEntity(CreateDishAvailabilityDto dto) {
        if (dto == null) return null;
        DishAvailability e = new DishAvailability();
        e.setDishId(dto.getDishId());
        e.setDayOfWeek(dto.getDayOfWeek());
        e.setStartTime(dto.getStartTime());
        e.setEndTime(dto.getEndTime());
        return e;
    }
}

