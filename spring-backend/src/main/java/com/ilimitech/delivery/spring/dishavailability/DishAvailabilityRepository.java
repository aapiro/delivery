package com.ilimitech.delivery.spring.dishavailability;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishAvailabilityRepository extends JpaRepository<DishAvailability, Long> {
}

