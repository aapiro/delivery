package com.ilimitech.delivery.spring.dishes;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "dishes")
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long restaurantId;
    private Long categoryId;

    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    private String imageUrl;
    private Boolean isAvailable;
    private Boolean isPopular;
    private Integer preparationTime;
    private Integer stock;
    private Boolean stockUnlimited;

    @Column(precision = 5, scale = 2)
    private BigDecimal popularityScore;

    private LocalDateTime deletedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Dish() {}

    public Dish(Long id, Long restaurantId, Long categoryId, String name, String description, BigDecimal price, String imageUrl, Boolean isAvailable, Boolean isPopular, Integer preparationTime, Integer stock, Boolean stockUnlimited, BigDecimal popularityScore, LocalDateTime deletedAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isAvailable = isAvailable;
        this.isPopular = isPopular;
        this.preparationTime = preparationTime;
        this.stock = stock;
        this.stockUnlimited = stockUnlimited;
        this.popularityScore = popularityScore;
        this.deletedAt = deletedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id; private Long restaurantId; private Long categoryId; private String name; private String description; private java.math.BigDecimal price; private String imageUrl; private Boolean isAvailable; private Boolean isPopular; private Integer preparationTime; private Integer stock; private Boolean stockUnlimited; private java.math.BigDecimal popularityScore; private java.time.LocalDateTime deletedAt; private java.time.LocalDateTime createdAt; private java.time.LocalDateTime updatedAt;
        public Builder id(Long id){ this.id = id; return this; }
        public Builder restaurantId(Long restaurantId){ this.restaurantId = restaurantId; return this; }
        public Builder categoryId(Long categoryId){ this.categoryId = categoryId; return this; }
        public Builder name(String name){ this.name = name; return this; }
        public Builder description(String description){ this.description = description; return this; }
        public Builder price(java.math.BigDecimal price){ this.price = price; return this; }
        public Builder imageUrl(String imageUrl){ this.imageUrl = imageUrl; return this; }
        public Builder isAvailable(Boolean isAvailable){ this.isAvailable = isAvailable; return this; }
        public Builder isPopular(Boolean isPopular){ this.isPopular = isPopular; return this; }
        public Builder preparationTime(Integer preparationTime){ this.preparationTime = preparationTime; return this; }
        public Builder stock(Integer stock){ this.stock = stock; return this; }
        public Builder stockUnlimited(Boolean stockUnlimited){ this.stockUnlimited = stockUnlimited; return this; }
        public Builder popularityScore(java.math.BigDecimal popularityScore){ this.popularityScore = popularityScore; return this; }
        public Builder deletedAt(java.time.LocalDateTime deletedAt){ this.deletedAt = deletedAt; return this; }
        public Builder createdAt(java.time.LocalDateTime createdAt){ this.createdAt = createdAt; return this; }
        public Builder updatedAt(java.time.LocalDateTime updatedAt){ this.updatedAt = updatedAt; return this; }
        public Dish build(){ return new Dish(id,restaurantId,categoryId,name,description,price,imageUrl,isAvailable,isPopular,preparationTime,stock,stockUnlimited,popularityScore,deletedAt,createdAt,updatedAt); }
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public Boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; }
    public Boolean getIsPopular() { return isPopular; }
    public void setIsPopular(Boolean isPopular) { this.isPopular = isPopular; }
    public Integer getPreparationTime() { return preparationTime; }
    public void setPreparationTime(Integer preparationTime) { this.preparationTime = preparationTime; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public Boolean getStockUnlimited() { return stockUnlimited; }
    public void setStockUnlimited(Boolean stockUnlimited) { this.stockUnlimited = stockUnlimited; }
    public BigDecimal getPopularityScore() { return popularityScore; }
    public void setPopularityScore(BigDecimal popularityScore) { this.popularityScore = popularityScore; }
    public LocalDateTime getDeletedAt() { return deletedAt; }
    public void setDeletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

