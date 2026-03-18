package com.ilimitech.delivery.spring.orderstatushistory.dto;
import java.time.LocalDateTime;
public class OrderStatusHistoryDto {
    private Long id; private Long orderId; private String status; private Long changedByUserId; private String notes; private LocalDateTime createdAt;
    public OrderStatusHistoryDto() {}
    public Long getId() { return id; } public void setId(Long v) { this.id = v; }
    public Long getOrderId() { return orderId; } public void setOrderId(Long v) { this.orderId = v; }
    public String getStatus() { return status; } public void setStatus(String v) { this.status = v; }
    public Long getChangedByUserId() { return changedByUserId; } public void setChangedByUserId(Long v) { this.changedByUserId = v; }
    public String getNotes() { return notes; } public void setNotes(String v) { this.notes = v; }
    public LocalDateTime getCreatedAt() { return createdAt; } public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }
}

