package com.ilimitech.delivery.spring.orderitems;

import com.ilimitech.delivery.spring.orderitems.dto.CreateOrderItemDto;
import com.ilimitech.delivery.spring.orderitems.dto.OrderItemDto;
import com.ilimitech.delivery.spring.orderitems.dto.UpdateOrderItemDto;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrderItemMapper {

    public OrderItemDto toDto(OrderItem o){
        if (o == null) return null;
        OrderItemDto d = new OrderItemDto();
        d.setId(o.getId());
        d.setOrderId(o.getOrderId());
        d.setDishId(o.getDishId());
        d.setQuantity(o.getQuantity());
        d.setPrice(o.getPrice());
        return d;
    }

    public OrderItem toEntity(CreateOrderItemDto d){
        if (d == null) return null;
        OrderItem o = new OrderItem();
        o.setOrderId(d.getOrderId());
        o.setDishId(d.getDishId());
        o.setQuantity(d.getQuantity());
        o.setPrice(d.getPrice());
        return o;
    }

    public OrderItem applyUpdate(OrderItem existing, UpdateOrderItemDto d){
        Optional.ofNullable(d.getQuantity()).ifPresent(existing::setQuantity);
        Optional.ofNullable(d.getPrice()).ifPresent(existing::setPrice);
        return existing;
    }
}

