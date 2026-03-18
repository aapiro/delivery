package com.ilimitech.delivery.spring.orderissues.dto;
import jakarta.validation.constraints.NotNull;
public class CreateOrderIssueDto {
    @NotNull private Long orderId;
    private String type;
    private String description;
    public CreateOrderIssueDto() {}
    public Long getOrderId() { return orderId; } public void setOrderId(Long v) { this.orderId = v; }
    public String getType() { return type; } public void setType(String v) { this.type = v; }
    public String getDescription() { return description; } public void setDescription(String v) { this.description = v; }
}

