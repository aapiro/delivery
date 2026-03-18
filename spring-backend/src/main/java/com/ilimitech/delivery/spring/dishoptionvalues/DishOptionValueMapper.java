package com.ilimitech.delivery.spring.dishoptionvalues;

import com.ilimitech.delivery.spring.dishoptionvalues.dto.CreateDishOptionValueDto;
import com.ilimitech.delivery.spring.dishoptionvalues.dto.DishOptionValueDto;
import org.springframework.stereotype.Component;

@Component
public class DishOptionValueMapper {

    public DishOptionValueDto toDto(DishOptionValue e) {
        if (e == null) return null;
        DishOptionValueDto dto = new DishOptionValueDto();
        dto.setId(e.getId());
        dto.setOptionId(e.getOptionId());
        dto.setName(e.getName());
        dto.setExtraPrice(e.getExtraPrice());
        return dto;
    }

    public DishOptionValue toEntity(CreateDishOptionValueDto dto) {
        if (dto == null) return null;
        DishOptionValue e = new DishOptionValue();
        e.setOptionId(dto.getOptionId());
        e.setName(dto.getName());
        e.setExtraPrice(dto.getExtraPrice());
        return e;
    }
}

