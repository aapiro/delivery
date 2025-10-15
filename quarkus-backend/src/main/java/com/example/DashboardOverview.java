package com.example;

import java.math.BigDecimal;

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
    public DashboardOverview() {}
    
    public DashboardOverview(String message) {
        this.message = message;
    }
    
    public DashboardOverview(String message, long totalRestaurants, long activeRestaurants,
                           long totalDishes, long availableDishes, long totalOrders,
                           long recentOrders, BigDecimal totalRevenue) {
        this.message = message;
        this.totalRestaurants = totalRestaurants;
        this.activeRestaurants = activeRestaurants;
        this.totalDishes = totalDishes;
        this.availableDishes = availableDishes;
        this.totalOrders = totalOrders;
        this.recentOrders = recentOrders;
        this.totalRevenue = totalRevenue;
    }
    
    // Getters and setters
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }

    public long getTotalRestaurants() {
        return totalRestaurants;
    }

    public void setTotalRestaurants(long totalRestaurants) {
        this.totalRestaurants = totalRestaurants;
    }

    public long getActiveRestaurants() {
        return activeRestaurants;
    }

    public void setActiveRestaurants(long activeRestaurants) {
        this.activeRestaurants = activeRestaurants;
    }

    public long getTotalDishes() {
        return totalDishes;
    }

    public void setTotalDishes(long totalDishes) {
        this.totalDishes = totalDishes;
    }

    public long getAvailableDishes() {
        return availableDishes;
    }

    public void setAvailableDishes(long availableDishes) {
        this.availableDishes = availableDishes;
    }

    public long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public long getRecentOrders() {
        return recentOrders;
    }

    public void setRecentOrders(long recentOrders) {
        this.recentOrders = recentOrders;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}