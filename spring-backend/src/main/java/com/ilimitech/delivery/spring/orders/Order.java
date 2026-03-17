package com.ilimitech.delivery.spring.orders;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long restaurantId;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(precision = 10, scale = 2)
    private BigDecimal deliveryFee;

    private String deliveryType;

    @Column(precision = 10, scale = 2)
    private BigDecimal serviceFee;

    @Column(precision = 10, scale = 2)
    private BigDecimal taxAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal discountAmount;

    private String status;

    @Column(columnDefinition = "text")
    private String deliveryAddress;

    private LocalDateTime orderDate;

    private Long courierId;
    private LocalDateTime estimatedDeliveryTime;
    private LocalDateTime scheduledDeliveryTime;
    private LocalDateTime actualDeliveryTime;
    private LocalDateTime preparationStartedAt;
    private LocalDateTime pickedUpAt;

    @Column(precision = 10, scale = 2)
    private BigDecimal platformCommission;

    @Column(precision = 10, scale = 2)
    private BigDecimal restaurantEarnings;

    @Column(precision = 10, scale = 2)
    private BigDecimal courierEarnings;

    @Column(columnDefinition = "text")
    private String cancellationReason;

    private String cancelledBy;

    @Column(columnDefinition = "text")
    private String specialInstructions;

    public Order() {}

    public Order(Long id, Long userId, Long restaurantId, BigDecimal totalAmount, BigDecimal subtotal, BigDecimal deliveryFee, String deliveryType, BigDecimal serviceFee, BigDecimal taxAmount, BigDecimal discountAmount, String status, String deliveryAddress, LocalDateTime orderDate, Long courierId, LocalDateTime estimatedDeliveryTime, LocalDateTime scheduledDeliveryTime, LocalDateTime actualDeliveryTime, LocalDateTime preparationStartedAt, LocalDateTime pickedUpAt, BigDecimal platformCommission, BigDecimal restaurantEarnings, BigDecimal courierEarnings, String cancellationReason, String cancelledBy, String specialInstructions) {
        this.id = id;
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.totalAmount = totalAmount;
        this.subtotal = subtotal;
        this.deliveryFee = deliveryFee;
        this.deliveryType = deliveryType;
        this.serviceFee = serviceFee;
        this.taxAmount = taxAmount;
        this.discountAmount = discountAmount;
        this.status = status;
        this.deliveryAddress = deliveryAddress;
        this.orderDate = orderDate;
        this.courierId = courierId;
        this.estimatedDeliveryTime = estimatedDeliveryTime;
        this.scheduledDeliveryTime = scheduledDeliveryTime;
        this.actualDeliveryTime = actualDeliveryTime;
        this.preparationStartedAt = preparationStartedAt;
        this.pickedUpAt = pickedUpAt;
        this.platformCommission = platformCommission;
        this.restaurantEarnings = restaurantEarnings;
        this.courierEarnings = courierEarnings;
        this.cancellationReason = cancellationReason;
        this.cancelledBy = cancelledBy;
        this.specialInstructions = specialInstructions;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id; private Long userId; private Long restaurantId; private BigDecimal totalAmount; private BigDecimal subtotal; private BigDecimal deliveryFee; private String deliveryType; private BigDecimal serviceFee; private BigDecimal taxAmount; private BigDecimal discountAmount; private String status; private String deliveryAddress; private java.time.LocalDateTime orderDate; private Long courierId; private java.time.LocalDateTime estimatedDeliveryTime; private java.time.LocalDateTime scheduledDeliveryTime; private java.time.LocalDateTime actualDeliveryTime; private java.time.LocalDateTime preparationStartedAt; private java.time.LocalDateTime pickedUpAt; private BigDecimal platformCommission; private BigDecimal restaurantEarnings; private BigDecimal courierEarnings; private String cancellationReason; private String cancelledBy; private String specialInstructions;

        public Builder id(Long id){ this.id = id; return this; }
        public Builder userId(Long userId){ this.userId = userId; return this; }
        public Builder restaurantId(Long restaurantId){ this.restaurantId = restaurantId; return this; }
        public Builder totalAmount(BigDecimal totalAmount){ this.totalAmount = totalAmount; return this; }
        public Builder subtotal(BigDecimal subtotal){ this.subtotal = subtotal; return this; }
        public Builder deliveryFee(BigDecimal deliveryFee){ this.deliveryFee = deliveryFee; return this; }
        public Builder deliveryType(String deliveryType){ this.deliveryType = deliveryType; return this; }
        public Builder serviceFee(BigDecimal serviceFee){ this.serviceFee = serviceFee; return this; }
        public Builder taxAmount(BigDecimal taxAmount){ this.taxAmount = taxAmount; return this; }
        public Builder discountAmount(BigDecimal discountAmount){ this.discountAmount = discountAmount; return this; }
        public Builder status(String status){ this.status = status; return this; }
        public Builder deliveryAddress(String deliveryAddress){ this.deliveryAddress = deliveryAddress; return this; }
        public Builder orderDate(java.time.LocalDateTime orderDate){ this.orderDate = orderDate; return this; }
        public Builder courierId(Long courierId){ this.courierId = courierId; return this; }
        public Builder estimatedDeliveryTime(java.time.LocalDateTime estimatedDeliveryTime){ this.estimatedDeliveryTime = estimatedDeliveryTime; return this; }
        public Builder scheduledDeliveryTime(java.time.LocalDateTime scheduledDeliveryTime){ this.scheduledDeliveryTime = scheduledDeliveryTime; return this; }
        public Builder actualDeliveryTime(java.time.LocalDateTime actualDeliveryTime){ this.actualDeliveryTime = actualDeliveryTime; return this; }
        public Builder preparationStartedAt(java.time.LocalDateTime preparationStartedAt){ this.preparationStartedAt = preparationStartedAt; return this; }
        public Builder pickedUpAt(java.time.LocalDateTime pickedUpAt){ this.pickedUpAt = pickedUpAt; return this; }
        public Builder platformCommission(BigDecimal platformCommission){ this.platformCommission = platformCommission; return this; }
        public Builder restaurantEarnings(BigDecimal restaurantEarnings){ this.restaurantEarnings = restaurantEarnings; return this; }
        public Builder courierEarnings(BigDecimal courierEarnings){ this.courierEarnings = courierEarnings; return this; }
        public Builder cancellationReason(String cancellationReason){ this.cancellationReason = cancellationReason; return this; }
        public Builder cancelledBy(String cancelledBy){ this.cancelledBy = cancelledBy; return this; }
        public Builder specialInstructions(String specialInstructions){ this.specialInstructions = specialInstructions; return this; }

        public Order build(){ return new Order(id,userId,restaurantId,totalAmount,subtotal,deliveryFee,deliveryType,serviceFee,taxAmount,discountAmount,status,deliveryAddress,orderDate,courierId,estimatedDeliveryTime,scheduledDeliveryTime,actualDeliveryTime,preparationStartedAt,pickedUpAt,platformCommission,restaurantEarnings,courierEarnings,cancellationReason,cancelledBy,specialInstructions); }
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public BigDecimal getDeliveryFee() { return deliveryFee; }
    public void setDeliveryFee(BigDecimal deliveryFee) { this.deliveryFee = deliveryFee; }
    public String getDeliveryType() { return deliveryType; }
    public void setDeliveryType(String deliveryType) { this.deliveryType = deliveryType; }
    public BigDecimal getServiceFee() { return serviceFee; }
    public void setServiceFee(BigDecimal serviceFee) { this.serviceFee = serviceFee; }
    public BigDecimal getTaxAmount() { return taxAmount; }
    public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }
    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    public Long getCourierId() { return courierId; }
    public void setCourierId(Long courierId) { this.courierId = courierId; }
    public LocalDateTime getEstimatedDeliveryTime() { return estimatedDeliveryTime; }
    public void setEstimatedDeliveryTime(LocalDateTime estimatedDeliveryTime) { this.estimatedDeliveryTime = estimatedDeliveryTime; }
    public LocalDateTime getScheduledDeliveryTime() { return scheduledDeliveryTime; }
    public void setScheduledDeliveryTime(LocalDateTime scheduledDeliveryTime) { this.scheduledDeliveryTime = scheduledDeliveryTime; }
    public LocalDateTime getActualDeliveryTime() { return actualDeliveryTime; }
    public void setActualDeliveryTime(LocalDateTime actualDeliveryTime) { this.actualDeliveryTime = actualDeliveryTime; }
    public LocalDateTime getPreparationStartedAt() { return preparationStartedAt; }
    public void setPreparationStartedAt(LocalDateTime preparationStartedAt) { this.preparationStartedAt = preparationStartedAt; }
    public LocalDateTime getPickedUpAt() { return pickedUpAt; }
    public void setPickedUpAt(LocalDateTime pickedUpAt) { this.pickedUpAt = pickedUpAt; }
    public BigDecimal getPlatformCommission() { return platformCommission; }
    public void setPlatformCommission(BigDecimal platformCommission) { this.platformCommission = platformCommission; }
    public BigDecimal getRestaurantEarnings() { return restaurantEarnings; }
    public void setRestaurantEarnings(BigDecimal restaurantEarnings) { this.restaurantEarnings = restaurantEarnings; }
    public BigDecimal getCourierEarnings() { return courierEarnings; }
    public void setCourierEarnings(BigDecimal courierEarnings) { this.courierEarnings = courierEarnings; }
    public String getCancellationReason() { return cancellationReason; }
    public void setCancellationReason(String cancellationReason) { this.cancellationReason = cancellationReason; }
    public String getCancelledBy() { return cancelledBy; }
    public void setCancelledBy(String cancelledBy) { this.cancelledBy = cancelledBy; }
    public String getSpecialInstructions() { return specialInstructions; }
    public void setSpecialInstructions(String specialInstructions) { this.specialInstructions = specialInstructions; }
}

