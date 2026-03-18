package com.ilimitech.delivery.spring.ordertracking.dto;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public class CreateOrderTrackingDto {
    @NotNull private Long orderId;
    private Long courierId;
    private BigDecimal latitude; private BigDecimal longitude;
    private String status; private LocalDateTime estimatedArrival;
    public CreateOrderTrackingDto() {}
    public Long getOrderId() { return orderId; } public void setOrderId(Long v) { this.orderId = v; }
    public Long getCourierId() { return courierId; } public void setCourierId(Long v) { this.courierId = v; }
    public BigDecimal getLatitude() { return latitude; } public void setLatitude(BigDecimal v) { this.latitude = v; }
    public BigDecimal getLongitude() { return longitude; } public void setLongitude(BigDecimal v) { this.longitude = v; }
    public String getStatus() { return status; } public void setStatus(String v) { this.status = v; }
    public LocalDateTime getEstimatedArrival() { return estimatedArrival; } public void setEstimatedArrival(LocalDateTime v) { this.estimatedArrival = v; }
}

