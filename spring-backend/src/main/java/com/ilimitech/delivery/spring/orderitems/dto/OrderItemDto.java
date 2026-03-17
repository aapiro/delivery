package com.ilimitech.delivery.spring.orderitems.dto;

import java.math.BigDecimal;

public class OrderItemDto {
    private Long id;
    private Long orderId;
    private Long dishId;
    private Integer quantity;
    private BigDecimal price;

    public OrderItemDto() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getDishId() { return dishId; }
    public void setDishId(Long dishId) { this.dishId = dishId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}

