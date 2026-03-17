package com.ilimitech.delivery.spring.orders;

import com.ilimitech.delivery.spring.orders.dto.CreateOrderDto;
import com.ilimitech.delivery.spring.orders.dto.OrderDto;
import com.ilimitech.delivery.spring.orders.dto.UpdateOrderDto;

import java.util.List;

public interface OrderService {
    List<OrderDto> findAll();
    OrderDto findById(Long id);
    OrderDto create(CreateOrderDto dto);
    OrderDto update(Long id, UpdateOrderDto dto);
    void delete(Long id);
}

