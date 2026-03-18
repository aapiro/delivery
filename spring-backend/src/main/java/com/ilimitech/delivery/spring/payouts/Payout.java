package com.ilimitech.delivery.spring.payouts;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "payouts")
public class Payout {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String recipientType;
    private Long recipientId;
    @Column(precision = 10, scale = 2) private BigDecimal amount;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private String status;
    private LocalDateTime processedAt;

    public Payout() {}
    public Long getId() { return id; } public void setId(Long v) { this.id = v; }
    public String getRecipientType() { return recipientType; } public void setRecipientType(String v) { this.recipientType = v; }
    public Long getRecipientId() { return recipientId; } public void setRecipientId(Long v) { this.recipientId = v; }
    public BigDecimal getAmount() { return amount; } public void setAmount(BigDecimal v) { this.amount = v; }
    public LocalDate getPeriodStart() { return periodStart; } public void setPeriodStart(LocalDate v) { this.periodStart = v; }
    public LocalDate getPeriodEnd() { return periodEnd; } public void setPeriodEnd(LocalDate v) { this.periodEnd = v; }
    public String getStatus() { return status; } public void setStatus(String v) { this.status = v; }
    public LocalDateTime getProcessedAt() { return processedAt; } public void setProcessedAt(LocalDateTime v) { this.processedAt = v; }
}

