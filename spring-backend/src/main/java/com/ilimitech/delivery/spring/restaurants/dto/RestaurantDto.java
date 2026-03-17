package com.ilimitech.delivery.spring.restaurants.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RestaurantDto {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private Boolean isOpen;
    private Boolean isActive;
    private BigDecimal minimumOrder;
    private BigDecimal deliveryFee;
    private Integer deliveryTimeMin;
    private Integer deliveryTimeMax;
    private BigDecimal rating;
    private Integer reviewCount;
    private Integer maxActiveOrders;
    private Integer avgPreparationTime;
    private Integer avgDeliveryTime;
    private BigDecimal cancellationRate;
    private BigDecimal qualityScore;
    private LocalDateTime deletedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public RestaurantDto() {}

    // Getters and setters omitted for brevity (generated below)

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public Boolean getIsOpen() { return isOpen; }
    public void setIsOpen(Boolean isOpen) { this.isOpen = isOpen; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public BigDecimal getMinimumOrder() { return minimumOrder; }
    public void setMinimumOrder(BigDecimal minimumOrder) { this.minimumOrder = minimumOrder; }
    public BigDecimal getDeliveryFee() { return deliveryFee; }
    public void setDeliveryFee(BigDecimal deliveryFee) { this.deliveryFee = deliveryFee; }
    public Integer getDeliveryTimeMin() { return deliveryTimeMin; }
    public void setDeliveryTimeMin(Integer deliveryTimeMin) { this.deliveryTimeMin = deliveryTimeMin; }
    public Integer getDeliveryTimeMax() { return deliveryTimeMax; }
    public void setDeliveryTimeMax(Integer deliveryTimeMax) { this.deliveryTimeMax = deliveryTimeMax; }
    public BigDecimal getRating() { return rating; }
    public void setRating(BigDecimal rating) { this.rating = rating; }
    public Integer getReviewCount() { return reviewCount; }
    public void setReviewCount(Integer reviewCount) { this.reviewCount = reviewCount; }
    public Integer getMaxActiveOrders() { return maxActiveOrders; }
    public void setMaxActiveOrders(Integer maxActiveOrders) { this.maxActiveOrders = maxActiveOrders; }
    public Integer getAvgPreparationTime() { return avgPreparationTime; }
    public void setAvgPreparationTime(Integer avgPreparationTime) { this.avgPreparationTime = avgPreparationTime; }
    public Integer getAvgDeliveryTime() { return avgDeliveryTime; }
    public void setAvgDeliveryTime(Integer avgDeliveryTime) { this.avgDeliveryTime = avgDeliveryTime; }
    public BigDecimal getCancellationRate() { return cancellationRate; }
    public void setCancellationRate(BigDecimal cancellationRate) { this.cancellationRate = cancellationRate; }
    public BigDecimal getQualityScore() { return qualityScore; }
    public void setQualityScore(BigDecimal qualityScore) { this.qualityScore = qualityScore; }
    public LocalDateTime getDeletedAt() { return deletedAt; }
    public void setDeletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

