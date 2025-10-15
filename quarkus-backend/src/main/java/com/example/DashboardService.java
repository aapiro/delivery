package com.example;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class DashboardService {

    @Inject
    RestaurantRepository restaurantRepository;

    @Inject 
    DishRepository dishRepository;

    @Inject
    OrderRepository orderRepository;

    public DashboardStats getDashboardStatistics() {
        // Get counts for different entities
        long totalRestaurants = restaurantRepository.count();
        long activeRestaurants = restaurantRepository.listAll().stream()
                .filter(Restaurant::isOpen)
                .count();
        
        long totalDishes = dishRepository.count();
        long availableDishes = dishRepository.listAll().stream()
                .filter(Dish::isAvailable)
                .count();
                
        // Get order statistics
        List<Order> orders = orderRepository.listAll();
        long totalOrders = orders.size();
        
        // Calculate revenue (sum of all completed orders)
        BigDecimal totalRevenue = orders.stream()
                .filter(order -> order.getStatus() == OrderStatus.DELIVERED)
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
                
        // Get recent orders (last 7 days)
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        long recentOrders = orders.stream()
                .filter(order -> order.getOrderDate().isAfter(sevenDaysAgo))
                .count();
        
        return new DashboardStats(
            totalRestaurants,
            activeRestaurants,
            totalDishes,
            availableDishes,
            totalOrders,
            recentOrders,
            totalRevenue
        );
    }
    
    public static class DashboardStats {
        private long totalRestaurants;
        private long activeRestaurants;
        private long totalDishes;
        private long availableDishes;
        private long totalOrders;
        private long recentOrders; // Orders in last 7 days
        private BigDecimal totalRevenue;

        public DashboardStats(long totalRestaurants, long activeRestaurants, long totalDishes, 
                            long availableDishes, long totalOrders, long recentOrders, 
                            BigDecimal totalRevenue) {
            this.totalRestaurants = totalRestaurants;
            this.activeRestaurants = activeRestaurants;
            this.totalDishes = totalDishes;
            this.availableDishes = availableDishes;
            this.totalOrders = totalOrders;
            this.recentOrders = recentOrders;
            this.totalRevenue = totalRevenue;
        }

        // Getters
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
}