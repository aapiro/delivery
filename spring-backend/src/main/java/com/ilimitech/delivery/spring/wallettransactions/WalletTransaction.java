package com.ilimitech.delivery.spring.wallettransactions;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "wallet_transactions")
public class WalletTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long walletId;

    private String type; // CREDIT, DEBIT, REFUND, REFERRAL_BONUS

    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

    private Long orderId;

    @Column(columnDefinition = "text")
    private String description;

    private LocalDateTime createdAt;

    public WalletTransaction() {}

    public WalletTransaction(Long id, Long walletId, String type, BigDecimal amount, Long orderId, String description, LocalDateTime createdAt) {
        this.id = id;
        this.walletId = walletId;
        this.type = type;
        this.amount = amount;
        this.orderId = orderId;
        this.description = description;
        this.createdAt = createdAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Long walletId;
        private String type;
        private BigDecimal amount;
        private Long orderId;
        private String description;
        private LocalDateTime createdAt;

        public Builder id(Long id){ this.id = id; return this; }
        public Builder walletId(Long walletId){ this.walletId = walletId; return this; }
        public Builder type(String type){ this.type = type; return this; }
        public Builder amount(BigDecimal amount){ this.amount = amount; return this; }
        public Builder orderId(Long orderId){ this.orderId = orderId; return this; }
        public Builder description(String description){ this.description = description; return this; }
        public Builder createdAt(LocalDateTime createdAt){ this.createdAt = createdAt; return this; }
        public WalletTransaction build(){ return new WalletTransaction(id,walletId,type,amount,orderId,description,createdAt); }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getWalletId() { return walletId; }
    public void setWalletId(Long walletId) { this.walletId = walletId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

