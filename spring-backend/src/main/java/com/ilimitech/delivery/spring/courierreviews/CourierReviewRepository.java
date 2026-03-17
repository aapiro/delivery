package com.ilimitech.delivery.spring.courierreviews;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourierReviewRepository extends JpaRepository<CourierReview, Long> {
}

