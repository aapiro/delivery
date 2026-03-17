package com.ilimitech.delivery.spring.courierreviews;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "courier_reviews")
public class CourierReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long courierId;
    private Long orderId;
    private Long userId;
    private Integer rating;

    @Column(columnDefinition = "text")
    private String comment;

    private LocalDateTime createdAt;

    public CourierReview() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCourierId() { return courierId; }
    public void setCourierId(Long courierId) { this.courierId = courierId; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

