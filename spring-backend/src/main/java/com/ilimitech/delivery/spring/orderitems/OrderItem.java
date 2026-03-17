package com.ilimitech.delivery.spring.orderitems;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;
    private Long dishId;
    private Integer quantity;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    public OrderItem() {}

    public OrderItem(Long id, Long orderId, Long dishId, Integer quantity, BigDecimal price) {
        this.id = id;
        this.orderId = orderId;
        this.dishId = dishId;
        this.quantity = quantity;
        this.price = price;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id; private Long orderId; private Long dishId; private Integer quantity; private java.math.BigDecimal price;
        public Builder id(Long id){ this.id = id; return this; }
        public Builder orderId(Long orderId){ this.orderId = orderId; return this; }
        public Builder dishId(Long dishId){ this.dishId = dishId; return this; }
        public Builder quantity(Integer quantity){ this.quantity = quantity; return this; }
        public Builder price(java.math.BigDecimal price){ this.price = price; return this; }
        public OrderItem build(){ return new OrderItem(id,orderId,dishId,quantity,price); }
    }

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

