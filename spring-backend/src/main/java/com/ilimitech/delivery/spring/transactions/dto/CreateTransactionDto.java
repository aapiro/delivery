package com.ilimitech.delivery.spring.transactions.dto;

import java.math.BigDecimal;

public class CreateTransactionDto {
    private Long orderId;
    private Long paymentMethodId;
    private BigDecimal amount;
    private String status;
    private String providerTransactionId;

    public CreateTransactionDto() {}
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
}

