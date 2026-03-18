package com.ilimitech.delivery.spring.dishreviews;

import com.ilimitech.delivery.spring.dishreviews.dto.CreateDishReviewDto;
import com.ilimitech.delivery.spring.dishreviews.dto.DishReviewDto;

import java.util.List;

public interface DishReviewService {
    List<DishReviewDto> findAll();
    DishReviewDto create(CreateDishReviewDto dto);
}

