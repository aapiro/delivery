package com.ilimitech.delivery.spring.dishreviews;

import com.ilimitech.delivery.spring.dishreviews.dto.CreateDishReviewDto;
import com.ilimitech.delivery.spring.dishreviews.dto.DishReviewDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DishReviewMapper {

    public DishReviewDto toDto(DishReview e) {
        if (e == null) return null;
        DishReviewDto dto = new DishReviewDto();
        dto.setId(e.getId());
        dto.setDishId(e.getDishId());
        dto.setOrderId(e.getOrderId());
        dto.setUserId(e.getUserId());
        dto.setRating(e.getRating());
        dto.setComment(e.getComment());
        dto.setTags(e.getTags());
        dto.setCreatedAt(e.getCreatedAt());
        return dto;
    }

    public DishReview toEntity(CreateDishReviewDto dto) {
        if (dto == null) return null;
        DishReview e = new DishReview();
        e.setDishId(dto.getDishId());
        e.setOrderId(dto.getOrderId());
        e.setUserId(dto.getUserId());
        e.setRating(dto.getRating());
        e.setComment(dto.getComment());
        e.setTags(dto.getTags());
        e.setCreatedAt(LocalDateTime.now());
        return e;
    }
}

