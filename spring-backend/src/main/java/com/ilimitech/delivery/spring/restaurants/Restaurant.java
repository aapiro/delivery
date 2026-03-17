package com.ilimitech.delivery.spring.restaurants;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "restaurants")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "text")
    private String description;

    private String imageUrl;
    private Boolean isOpen;
    private Boolean isActive;

    @Column(precision = 10, scale = 2)
    private BigDecimal minimumOrder;

    @Column(precision = 10, scale = 2)
    private BigDecimal deliveryFee;

    private Integer deliveryTimeMin;
    private Integer deliveryTimeMax;

    @Column(precision = 3, scale = 2)
    private BigDecimal rating;

    private Integer reviewCount;
    private Integer maxActiveOrders;
    private Integer avgPreparationTime;
    private Integer avgDeliveryTime;

    @Column(precision = 5, scale = 2)
    private BigDecimal cancellationRate;

    @Column(precision = 5, scale = 2)
    private BigDecimal qualityScore;

    private LocalDateTime deletedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Restaurant() {}

    public Restaurant(Long id, String name, String description, String imageUrl, Boolean isOpen, Boolean isActive, BigDecimal minimumOrder, BigDecimal deliveryFee, Integer deliveryTimeMin, Integer deliveryTimeMax, BigDecimal rating, Integer reviewCount, Integer maxActiveOrders, Integer avgPreparationTime, Integer avgDeliveryTime, BigDecimal cancellationRate, BigDecimal qualityScore, LocalDateTime deletedAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.isOpen = isOpen;
        this.isActive = isActive;
        this.minimumOrder = minimumOrder;
        this.deliveryFee = deliveryFee;
        this.deliveryTimeMin = deliveryTimeMin;
        this.deliveryTimeMax = deliveryTimeMax;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.maxActiveOrders = maxActiveOrders;
        this.avgPreparationTime = avgPreparationTime;
        this.avgDeliveryTime = avgDeliveryTime;
        this.cancellationRate = cancellationRate;
        this.qualityScore = qualityScore;
        this.deletedAt = deletedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
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

        public Builder id(Long id){ this.id = id; return this; }
        public Builder name(String name){ this.name = name; return this; }
        public Builder description(String description){ this.description = description; return this; }
        public Builder imageUrl(String imageUrl){ this.imageUrl = imageUrl; return this; }
        public Builder isOpen(Boolean isOpen){ this.isOpen = isOpen; return this; }
        public Builder isActive(Boolean isActive){ this.isActive = isActive; return this; }
        public Builder minimumOrder(BigDecimal minimumOrder){ this.minimumOrder = minimumOrder; return this; }
        public Builder deliveryFee(BigDecimal deliveryFee){ this.deliveryFee = deliveryFee; return this; }
        public Builder deliveryTimeMin(Integer deliveryTimeMin){ this.deliveryTimeMin = deliveryTimeMin; return this; }
        public Builder deliveryTimeMax(Integer deliveryTimeMax){ this.deliveryTimeMax = deliveryTimeMax; return this; }
        public Builder rating(BigDecimal rating){ this.rating = rating; return this; }
        public Builder reviewCount(Integer reviewCount){ this.reviewCount = reviewCount; return this; }
        public Builder maxActiveOrders(Integer maxActiveOrders){ this.maxActiveOrders = maxActiveOrders; return this; }
        public Builder avgPreparationTime(Integer avgPreparationTime){ this.avgPreparationTime = avgPreparationTime; return this; }
        public Builder avgDeliveryTime(Integer avgDeliveryTime){ this.avgDeliveryTime = avgDeliveryTime; return this; }
        public Builder cancellationRate(BigDecimal cancellationRate){ this.cancellationRate = cancellationRate; return this; }
        public Builder qualityScore(BigDecimal qualityScore){ this.qualityScore = qualityScore; return this; }
        public Builder deletedAt(LocalDateTime deletedAt){ this.deletedAt = deletedAt; return this; }
        public Builder createdAt(LocalDateTime createdAt){ this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt){ this.updatedAt = updatedAt; return this; }

        public Restaurant build(){ return new Restaurant(id,name,description,imageUrl,isOpen,isActive,minimumOrder,deliveryFee,deliveryTimeMin,deliveryTimeMax,rating,reviewCount,maxActiveOrders,avgPreparationTime,avgDeliveryTime,cancellationRate,qualityScore,deletedAt,createdAt,updatedAt); }
    }

    // Getters and setters
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

