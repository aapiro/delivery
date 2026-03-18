package com.ilimitech.delivery.spring.orderstatushistory;
import com.ilimitech.delivery.spring.orderstatushistory.dto.*;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
@Component
public class OrderStatusHistoryMapper {
    public OrderStatusHistoryDto toDto(OrderStatusHistory e) {
        if (e == null) return null;
        OrderStatusHistoryDto d = new OrderStatusHistoryDto();
        d.setId(e.getId()); d.setOrderId(e.getOrderId()); d.setStatus(e.getStatus());
        d.setChangedByUserId(e.getChangedByUserId()); d.setNotes(e.getNotes()); d.setCreatedAt(e.getCreatedAt());
        return d;
    }
    public OrderStatusHistory toEntity(CreateOrderStatusHistoryDto dto) {
        OrderStatusHistory e = new OrderStatusHistory();
        e.setOrderId(dto.getOrderId()); e.setStatus(dto.getStatus());
        e.setChangedByUserId(dto.getChangedByUserId()); e.setNotes(dto.getNotes());
        e.setCreatedAt(LocalDateTime.now());
        return e;
    }
}

