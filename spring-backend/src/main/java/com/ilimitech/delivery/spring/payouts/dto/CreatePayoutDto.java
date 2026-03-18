package com.ilimitech.delivery.spring.payouts.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
public class CreatePayoutDto {
    @NotBlank private String recipientType;
    @NotNull private Long recipientId;
    @NotNull private BigDecimal amount;
    private LocalDate periodStart; private LocalDate periodEnd; private String status;
    public CreatePayoutDto() {}
    public String getRecipientType() { return recipientType; } public void setRecipientType(String v) { this.recipientType = v; }
    public Long getRecipientId() { return recipientId; } public void setRecipientId(Long v) { this.recipientId = v; }
    public BigDecimal getAmount() { return amount; } public void setAmount(BigDecimal v) { this.amount = v; }
    public LocalDate getPeriodStart() { return periodStart; } public void setPeriodStart(LocalDate v) { this.periodStart = v; }
    public LocalDate getPeriodEnd() { return periodEnd; } public void setPeriodEnd(LocalDate v) { this.periodEnd = v; }
    public String getStatus() { return status; } public void setStatus(String v) { this.status = v; }
}

