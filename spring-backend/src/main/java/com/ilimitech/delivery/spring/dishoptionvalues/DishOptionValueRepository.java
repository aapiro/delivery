package com.ilimitech.delivery.spring.dishoptionvalues;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishOptionValueRepository extends JpaRepository<DishOptionValue, Long> {
}

