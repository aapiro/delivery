package com.ilimitech.delivery.spring.orderstatushistory;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_status_history")
public class OrderStatusHistory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private Long orderId;
    private String status;
    private Long changedByUserId;
    @Column(columnDefinition = "text") private String notes;
    private LocalDateTime createdAt;

    public OrderStatusHistory() {}
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Long getOrderId() { return orderId; } public void setOrderId(Long v) { this.orderId = v; }
    public String getStatus() { return status; } public void setStatus(String v) { this.status = v; }
    public Long getChangedByUserId() { return changedByUserId; } public void setChangedByUserId(Long v) { this.changedByUserId = v; }
    public String getNotes() { return notes; } public void setNotes(String v) { this.notes = v; }
    public LocalDateTime getCreatedAt() { return createdAt; } public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }
}

