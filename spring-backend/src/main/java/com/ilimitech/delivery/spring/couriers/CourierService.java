package com.ilimitech.delivery.spring.couriers;

import com.ilimitech.delivery.spring.couriers.dto.CourierDto;
import com.ilimitech.delivery.spring.couriers.dto.CreateCourierDto;
import com.ilimitech.delivery.spring.couriers.dto.UpdateCourierDto;

import java.util.List;

public interface CourierService {
    List<CourierDto> findAll();
    CourierDto findById(Long id);
    CourierDto create(CreateCourierDto dto);
    CourierDto update(Long id, UpdateCourierDto dto);
    void delete(Long id);
}

