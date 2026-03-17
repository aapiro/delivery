package com.ilimitech.delivery.spring.wallettransactions.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class WalletTransactionDto {
    private Long id;
    private Long walletId;
    private String type;
    private BigDecimal amount;
    private Long orderId;
    private String description;
    private LocalDateTime createdAt;

    public WalletTransactionDto() {}
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

