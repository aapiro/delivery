package com.ilimitech.delivery.spring.tips.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TipDto {
    private Long id;
    private Long orderId;
    private Long courierId;
    private BigDecimal amount;
    private String tipType;
    private LocalDateTime createdAt;

    public TipDto() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getCourierId() { return courierId; }
    public void setCourierId(Long courierId) { this.courierId = courierId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getTipType() { return tipType; }
    public void setTipType(String tipType) { this.tipType = tipType; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

