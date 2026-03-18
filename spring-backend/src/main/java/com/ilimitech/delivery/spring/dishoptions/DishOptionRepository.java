package com.ilimitech.delivery.spring.dishoptions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishOptionRepository extends JpaRepository<DishOption, Long> {
}

