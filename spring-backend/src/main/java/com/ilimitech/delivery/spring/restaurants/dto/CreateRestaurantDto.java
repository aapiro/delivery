package com.ilimitech.delivery.spring.restaurants.dto;

import java.math.BigDecimal;

public class CreateRestaurantDto {
    private String name;
    private String description;
    private String imageUrl;
    private Boolean isOpen;
    private Boolean isActive;
    private BigDecimal minimumOrder;
    private BigDecimal deliveryFee;
    private Integer deliveryTimeMin;
    private Integer deliveryTimeMax;

    public CreateRestaurantDto() {}

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
}

