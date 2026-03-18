package com.ilimitech.delivery.spring.dishreviews.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CreateDishReviewDto {

    @NotNull
    private Long dishId;

    private Long orderId;

    @NotNull
    private Long userId;

    @NotNull
    @Min(1) @Max(5)
    private Integer rating;

    private String comment;
    private String tags;

    public CreateDishReviewDto() {}

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
}

