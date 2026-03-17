package com.ilimitech.delivery.spring.courierreviews;

import com.ilimitech.delivery.spring.courierreviews.dto.CourierReviewDto;
import com.ilimitech.delivery.spring.courierreviews.dto.CreateCourierReviewDto;

import java.util.List;

public interface CourierReviewService {
    List<CourierReviewDto> findAll();
    CourierReviewDto create(CreateCourierReviewDto dto);
}

