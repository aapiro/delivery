package com.ilimitech.delivery.spring.orderissues;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_issues")
public class OrderIssue {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderId;
    private String type;
    @Column(columnDefinition = "text")
    private String description;
    private LocalDateTime createdAt;

    public OrderIssue() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

