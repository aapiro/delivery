package com.ilimitech.delivery.spring.courierreviews;

import com.ilimitech.delivery.spring.courierreviews.dto.CourierReviewDto;
import com.ilimitech.delivery.spring.courierreviews.dto.CreateCourierReviewDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CourierReviewMapper {

    public CourierReviewDto toDto(CourierReview e) {
        if (e == null) return null;
        CourierReviewDto dto = new CourierReviewDto();
        dto.setId(e.getId());
        dto.setCourierId(e.getCourierId());
        dto.setOrderId(e.getOrderId());
        dto.setUserId(e.getUserId());
        dto.setRating(e.getRating());
        dto.setComment(e.getComment());
        dto.setCreatedAt(e.getCreatedAt());
        return dto;
    }

    public CourierReview toEntity(CreateCourierReviewDto dto) {
        if (dto == null) return null;
        CourierReview e = new CourierReview();
        e.setCourierId(dto.getCourierId());
        e.setOrderId(dto.getOrderId());
        e.setUserId(dto.getUserId());
        e.setRating(dto.getRating());
        e.setComment(dto.getComment());
        e.setCreatedAt(LocalDateTime.now());
        return e;
    }
}

