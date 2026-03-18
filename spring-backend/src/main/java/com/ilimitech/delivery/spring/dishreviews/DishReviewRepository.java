package com.ilimitech.delivery.spring.dishreviews;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishReviewRepository extends JpaRepository<DishReview, Long> {
}

