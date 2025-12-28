package com.ilimitech.delivery.application.port.out;

import com.ilimitech.delivery.infrastructure.adapter.out.persistence.DishEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DishRepository implements PanacheRepository<DishEntity> {
}