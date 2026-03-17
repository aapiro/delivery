package com.ilimitech.delivery.spring.transactions;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;
    private Long paymentMethodId;

    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

    private String status;
    private String providerTransactionId;
    private LocalDateTime createdAt;

    public Transaction() {}

    public Transaction(Long id, Long orderId, Long paymentMethodId, BigDecimal amount, String status, String providerTransactionId, LocalDateTime createdAt) {
        this.id = id;
        this.orderId = orderId;
        this.paymentMethodId = paymentMethodId;
        this.amount = amount;
        this.status = status;
        this.providerTransactionId = providerTransactionId;
        this.createdAt = createdAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Long orderId;
        private Long paymentMethodId;
        private BigDecimal amount;
        private String status;
        private String providerTransactionId;
        private LocalDateTime createdAt;

        public Builder id(Long id){ this.id = id; return this; }
        public Builder orderId(Long orderId){ this.orderId = orderId; return this; }
        public Builder paymentMethodId(Long paymentMethodId){ this.paymentMethodId = paymentMethodId; return this; }
        public Builder amount(BigDecimal amount){ this.amount = amount; return this; }
        public Builder status(String status){ this.status = status; return this; }
        public Builder providerTransactionId(String providerTransactionId){ this.providerTransactionId = providerTransactionId; return this; }
        public Builder createdAt(LocalDateTime createdAt){ this.createdAt = createdAt; return this; }
        public Transaction build(){ return new Transaction(id, orderId, paymentMethodId, amount, status, providerTransactionId, createdAt); }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getPaymentMethodId() { return paymentMethodId; }
    public void setPaymentMethodId(Long paymentMethodId) { this.paymentMethodId = paymentMethodId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getProviderTransactionId() { return providerTransactionId; }
    public void setProviderTransactionId(String providerTransactionId) { this.providerTransactionId = providerTransactionId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

