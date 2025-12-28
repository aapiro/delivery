package com.ilimitech.delivery.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DashboardOverview {
    private String message;
    private long totalRestaurants;
    private long activeRestaurants;
    private long totalDishes;
    private long availableDishes;
    private long totalOrders;
    private long recentOrders;
    private BigDecimal totalRevenue;

    // Default constructor for JSON serialization
    public DashboardOverview() {
    }

    public DashboardOverview(String message) {
        this.message = message;
    }

    public DashboardOverview(String message, long totalRestaurants, long activeRestaurants, long totalDishes, long availableDishes, long totalOrders, long recentOrders, BigDecimal totalRevenue) {
        this.message = message;
        this.totalRestaurants = totalRestaurants;
        this.activeRestaurants = activeRestaurants;
        this.totalDishes = totalDishes;
        this.availableDishes = availableDishes;
        this.totalOrders = totalOrders;
        this.recentOrders = recentOrders;
        this.totalRevenue = totalRevenue;
    }

}