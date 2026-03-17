package com.ilimitech.delivery.spring.orders;

import com.ilimitech.delivery.spring.orders.dto.CreateOrderDto;
import com.ilimitech.delivery.spring.orders.dto.OrderDto;
import com.ilimitech.delivery.spring.orders.dto.UpdateOrderDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class OrderMapper {

    public OrderDto toDto(Order o){
        if (o == null) return null;
        OrderDto d = new OrderDto();
        d.setId(o.getId());
        d.setUserId(o.getUserId());
        d.setRestaurantId(o.getRestaurantId());
        d.setTotalAmount(o.getTotalAmount());
        d.setSubtotal(o.getSubtotal());
        d.setDeliveryFee(o.getDeliveryFee());
        d.setDeliveryType(o.getDeliveryType());
        d.setServiceFee(o.getServiceFee());
        d.setTaxAmount(o.getTaxAmount());
        d.setDiscountAmount(o.getDiscountAmount());
        d.setStatus(o.getStatus());
        d.setDeliveryAddress(o.getDeliveryAddress());
        d.setOrderDate(o.getOrderDate());
        d.setCourierId(o.getCourierId());
        return d;
    }

    public Order toEntity(CreateOrderDto d){
        if (d == null) return null;
        Order o = new Order();
        o.setUserId(d.getUserId());
        o.setRestaurantId(d.getRestaurantId());
        o.setTotalAmount(d.getTotalAmount());
        o.setDeliveryType(d.getDeliveryType());
        o.setDeliveryAddress(d.getDeliveryAddress());
        o.setOrderDate(d.getOrderDate() != null ? d.getOrderDate() : LocalDateTime.now());
        o.setStatus("CREATED");
        return o;
    }

    public Order applyUpdate(Order existing, UpdateOrderDto d){
        Optional.ofNullable(d.getStatus()).ifPresent(existing::setStatus);
        Optional.ofNullable(d.getCourierId()).ifPresent(existing::setCourierId);
        Optional.ofNullable(d.getScheduledDeliveryTime()).ifPresent(existing::setScheduledDeliveryTime);
        Optional.ofNullable(d.getActualDeliveryTime()).ifPresent(existing::setActualDeliveryTime);
        return existing;
    }
}

