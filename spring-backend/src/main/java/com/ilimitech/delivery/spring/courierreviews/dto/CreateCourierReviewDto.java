package com.ilimitech.delivery.spring.courierreviews.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CreateCourierReviewDto {

    @NotNull
    private Long courierId;

    private Long orderId;

    @NotNull
    private Long userId;

    @NotNull
    @Min(1) @Max(5)
    private Integer rating;

    private String comment;

    public CreateCourierReviewDto() {}

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
}

