package com.ilimitech.delivery.spring.orderstatushistory.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
public class CreateOrderStatusHistoryDto {
    @NotNull private Long orderId;
    @NotBlank private String status;
    private Long changedByUserId;
    private String notes;
    public CreateOrderStatusHistoryDto() {}
    public Long getOrderId() { return orderId; } public void setOrderId(Long v) { this.orderId = v; }
    public String getStatus() { return status; } public void setStatus(String v) { this.status = v; }
    public Long getChangedByUserId() { return changedByUserId; } public void setChangedByUserId(Long v) { this.changedByUserId = v; }
    public String getNotes() { return notes; } public void setNotes(String v) { this.notes = v; }
}

