package com.ilimitech.delivery.restaurant;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RestaurantRepository implements PanacheRepository<Restaurant> {
}