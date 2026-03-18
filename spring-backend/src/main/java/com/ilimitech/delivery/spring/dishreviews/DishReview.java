package com.ilimitech.delivery.spring.dishreviews;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dish_reviews")
public class DishReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long dishId;
    private Long orderId;
    private Long userId;
    private Integer rating;

    @Column(columnDefinition = "text")
    private String comment;

    // Stored as comma-separated string (PostgreSQL array not supported in H2)
    private String tags;

    private LocalDateTime createdAt;

    public DishReview() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getDishId() { return dishId; }
    public void setDishId(Long dishId) { this.dishId = dishId; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

