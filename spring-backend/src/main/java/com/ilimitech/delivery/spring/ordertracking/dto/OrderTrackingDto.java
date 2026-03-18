package com.ilimitech.delivery.spring.ordertracking.dto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public class OrderTrackingDto {
    private Long id; private Long orderId; private Long courierId;
    private BigDecimal latitude; private BigDecimal longitude;
    private String status; private LocalDateTime estimatedArrival; private LocalDateTime createdAt;
    public OrderTrackingDto() {}
    public Long getId() { return id; } public void setId(Long v) { this.id = v; }
    public Long getOrderId() { return orderId; } public void setOrderId(Long v) { this.orderId = v; }
    public Long getCourierId() { return courierId; } public void setCourierId(Long v) { this.courierId = v; }
    public BigDecimal getLatitude() { return latitude; } public void setLatitude(BigDecimal v) { this.latitude = v; }
    public BigDecimal getLongitude() { return longitude; } public void setLongitude(BigDecimal v) { this.longitude = v; }
    public String getStatus() { return status; } public void setStatus(String v) { this.status = v; }
    public LocalDateTime getEstimatedArrival() { return estimatedArrival; } public void setEstimatedArrival(LocalDateTime v) { this.estimatedArrival = v; }
    public LocalDateTime getCreatedAt() { return createdAt; } public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }
}

