package com.ilimitech.delivery.application.usecase;

import com.ilimitech.delivery.common.NotFoundException;
import com.ilimitech.delivery.application.port.out.DishRepository;
import com.ilimitech.delivery.application.port.out.OrderRepository;
import com.ilimitech.delivery.infrastructure.adapter.out.persistence.Order;
import com.ilimitech.delivery.infrastructure.adapter.out.persistence.OrderItem;
import com.ilimitech.delivery.application.port.out.RestaurantRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class OrderService {

    @Inject
    OrderRepository orderRepository;

    @Inject
    RestaurantRepository restaurantRepository;

    @Inject
    DishRepository dishRepository;

    public List<Order> getAllOrders() {
        return orderRepository.listAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findByIdOptional(id);
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Transactional
    public Order createOrder(Order order) {
        // Set default values if not set
        if (order.getOrderDate() == null) {
            order.setOrderDate(LocalDateTime.now());
        }
        
        if (order.getStatus() == null) {
            order.setStatus(Order.OrderStatus.PENDING);
        }
        
        orderRepository.persist(order);
        return order;
    }

    @Transactional
    public Order updateOrderStatus(Long id, Order.OrderStatus status) {
        Optional<Order> optionalOrder = orderRepository.findByIdOptional(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(status);
            orderRepository.persist(order);
            return order;
        }
        throw new NotFoundException("Order not found with ID: " + id);
    }

    @Transactional
    public void cancelOrder(Long id) {
        Optional<Order> optionalOrder = orderRepository.findByIdOptional(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(Order.OrderStatus.CANCELLED);
            orderRepository.persist(order);
        } else {
            throw new NotFoundException("Order not found with ID: " + id);
        }
    }

    @Transactional
    public void deleteOrder(Long id) {
        Optional<Order> optionalOrder = orderRepository.findByIdOptional(id);
        if (optionalOrder.isPresent()) {
            orderRepository.delete(optionalOrder.get());
        } else {
            throw new NotFoundException("Order not found with ID: " + id);
        }
    }

    // Helper method to calculate total amount
    public BigDecimal calculateTotalAmount(List<OrderItem> items) {
        return items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}