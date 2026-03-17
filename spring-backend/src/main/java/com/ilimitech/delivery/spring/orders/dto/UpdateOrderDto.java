package com.ilimitech.delivery.spring.orders.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class UpdateOrderDto {
    private String status;
    private Long courierId;
    private LocalDateTime scheduledDeliveryTime;
    private LocalDateTime actualDeliveryTime;

    public UpdateOrderDto() {}
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getCourierId() { return courierId; }
    public void setCourierId(Long courierId) { this.courierId = courierId; }
    public LocalDateTime getScheduledDeliveryTime() { return scheduledDeliveryTime; }
    public void setScheduledDeliveryTime(LocalDateTime scheduledDeliveryTime) { this.scheduledDeliveryTime = scheduledDeliveryTime; }
    public LocalDateTime getActualDeliveryTime() { return actualDeliveryTime; }
    public void setActualDeliveryTime(LocalDateTime actualDeliveryTime) { this.actualDeliveryTime = actualDeliveryTime; }
}

