package com.ilimitech.delivery.spring.orderitems;

import com.ilimitech.delivery.spring.orderitems.dto.CreateOrderItemDto;
import com.ilimitech.delivery.spring.orderitems.dto.OrderItemDto;
import com.ilimitech.delivery.spring.orderitems.dto.UpdateOrderItemDto;

import java.util.List;

public interface OrderItemService {
    List<OrderItemDto> findAll();
    OrderItemDto findById(Long id);
    OrderItemDto create(CreateOrderItemDto dto);
    OrderItemDto update(Long id, UpdateOrderItemDto dto);
    void delete(Long id);
}

