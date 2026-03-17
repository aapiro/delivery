package com.ilimitech.delivery.spring.orderitems.dto;

import java.math.BigDecimal;

public class CreateOrderItemDto {
    private Long orderId;
    private Long dishId;
    private Integer quantity;
    private BigDecimal price;

    public CreateOrderItemDto() {}
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getDishId() { return dishId; }
    public void setDishId(Long dishId) { this.dishId = dishId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}

