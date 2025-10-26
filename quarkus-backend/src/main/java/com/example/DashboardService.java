package com.example;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
                .filter(order -> order.getStatus() == Order.OrderStatus.DELIVERED)
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
                
        // Get recent orders (last 7 days)
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        long recentOrders = orders.stream()
                .filter(order -> order.getOrderDate().isAfter(sevenDaysAgo))
                .count();
        
        // Order status distribution
        Map<Order.OrderStatus, Long> orderStatusDistribution = orders.stream()
                .collect(Collectors.groupingBy(Order::getStatus, Collectors.counting()));
                
        // Get top selling dishes (top 5)
        List<TopSellingDish> topSellingDishes = getTopSellingDishes(orders);
        
        return new DashboardStats(
            totalRestaurants,
            activeRestaurants,
            totalDishes,
            availableDishes,
            totalOrders,
            recentOrders,
            totalRevenue,
            orderStatusDistribution,
            topSellingDishes
        );
    }
    
    private List<TopSellingDish> getTopSellingDishes(List<Order> orders) {
        // This is a simplified implementation - in a real system, we would need to 
        // join with OrderItem table and aggregate by dish
        return orders.stream()
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.groupingBy(
                    orderItem -> orderItem.getDish(), 
                    Collectors.summingInt(OrderItem::getQuantity)
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<Dish, Integer>comparingByValue().reversed())
                .limit(5)
                .map(entry -> new TopSellingDish(entry.getKey().getName(), entry.getValue()))
                .collect(Collectors.toList());
    }
    
    public static class DashboardStats {
        private long totalRestaurants;
        private long activeRestaurants;
        private long totalDishes;
        private long availableDishes;
        private long totalOrders;
        private long recentOrders; // Orders in last 7 days
        private BigDecimal totalRevenue;
        private Map<Order.OrderStatus, Long> orderStatusDistribution;
        private List<TopSellingDish> topSellingDishes;

        public DashboardStats(long totalRestaurants, long activeRestaurants, long totalDishes, 
                            long availableDishes, long totalOrders, long recentOrders, 
                            BigDecimal totalRevenue, Map<Order.OrderStatus, Long> orderStatusDistribution,
                            List<TopSellingDish> topSellingDishes) {
            this.totalRestaurants = totalRestaurants;
            this.activeRestaurants = activeRestaurants;
            this.totalDishes = totalDishes;
            this.availableDishes = availableDishes;
            this.totalOrders = totalOrders;
            this.recentOrders = recentOrders;
            this.totalRevenue = totalRevenue;
            this.orderStatusDistribution = orderStatusDistribution;
            this.topSellingDishes = topSellingDishes;
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

        public Map<Order.OrderStatus, Long> getOrderStatusDistribution() {
            return orderStatusDistribution;
        }

        public void setOrderStatusDistribution(Map<Order.OrderStatus, Long> orderStatusDistribution) {
            this.orderStatusDistribution = orderStatusDistribution;
        }

        public List<TopSellingDish> getTopSellingDishes() {
            return topSellingDishes;
        }

        public void setTopSellingDishes(List<TopSellingDish> topSellingDishes) {
            this.topSellingDishes = topSellingDishes;
        }
    }
    
    // DTO for top selling dishes
    public static class TopSellingDish {
        private String dishName;
        private int quantitySold;

        public TopSellingDish(String dishName, int quantitySold) {
            this.dishName = dishName;
            this.quantitySold = quantitySold;
        }

        // Getters and setters
        public String getDishName() {
            return dishName;
        }

        public void setDishName(String dishName) {
            this.dishName = dishName;
        }

        public int getQuantitySold() {
            return quantitySold;
        }

        public void setQuantitySold(int quantitySold) {
            this.quantitySold = quantitySold;
        }
    }
}