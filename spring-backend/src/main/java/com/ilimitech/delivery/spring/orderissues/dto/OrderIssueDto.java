package com.ilimitech.delivery.spring.orderissues.dto;
import java.time.LocalDateTime;
public class OrderIssueDto {
    private Long id; private Long orderId; private String type; private String description; private LocalDateTime createdAt;
    public OrderIssueDto() {}
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Long getOrderId() { return orderId; } public void setOrderId(Long v) { this.orderId = v; }
    public String getType() { return type; } public void setType(String v) { this.type = v; }
    public String getDescription() { return description; } public void setDescription(String v) { this.description = v; }
    public LocalDateTime getCreatedAt() { return createdAt; } public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }
}

