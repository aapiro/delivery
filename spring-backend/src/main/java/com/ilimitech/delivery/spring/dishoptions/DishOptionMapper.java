package com.ilimitech.delivery.spring.dishoptions;

import com.ilimitech.delivery.spring.dishoptions.dto.CreateDishOptionDto;
import com.ilimitech.delivery.spring.dishoptions.dto.DishOptionDto;
import org.springframework.stereotype.Component;

@Component
public class DishOptionMapper {

    public DishOptionDto toDto(DishOption e) {
        if (e == null) return null;
        DishOptionDto dto = new DishOptionDto();
        dto.setId(e.getId());
        dto.setDishId(e.getDishId());
        dto.setName(e.getName());
        dto.setRequired(e.getRequired());
        return dto;
    }

    public DishOption toEntity(CreateDishOptionDto dto) {
        if (dto == null) return null;
        DishOption e = new DishOption();
        e.setDishId(dto.getDishId());
        e.setName(dto.getName());
        e.setRequired(dto.getRequired() != null ? dto.getRequired() : false);
        return e;
    }
}

