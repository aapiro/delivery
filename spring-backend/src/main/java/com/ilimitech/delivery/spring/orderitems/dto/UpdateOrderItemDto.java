package com.ilimitech.delivery.spring.orderitems.dto;

import java.math.BigDecimal;

public class UpdateOrderItemDto {
    private Integer quantity;
    private BigDecimal price;

    public UpdateOrderItemDto() {}
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}

