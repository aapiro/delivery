package com.ilimitech.delivery.spring.tips;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tips")
public class Tip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;
    private Long courierId;

    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

    private String tipType;
    private LocalDateTime createdAt;

    public Tip() {}

    public Tip(Long id, Long orderId, Long courierId, BigDecimal amount, String tipType, LocalDateTime createdAt) {
        this.id = id;
        this.orderId = orderId;
        this.courierId = courierId;
        this.amount = amount;
        this.tipType = tipType;
        this.createdAt = createdAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Long orderId;
        private Long courierId;
        private BigDecimal amount;
        private String tipType;
        private LocalDateTime createdAt;

        public Builder id(Long id){ this.id = id; return this; }
        public Builder orderId(Long orderId){ this.orderId = orderId; return this; }
        public Builder courierId(Long courierId){ this.courierId = courierId; return this; }
        public Builder amount(BigDecimal amount){ this.amount = amount; return this; }
        public Builder tipType(String tipType){ this.tipType = tipType; return this; }
        public Builder createdAt(LocalDateTime createdAt){ this.createdAt = createdAt; return this; }
        public Tip build(){ return new Tip(id, orderId, courierId, amount, tipType, createdAt); }
    }

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

