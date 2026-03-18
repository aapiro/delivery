package com.ilimitech.delivery.spring.ordertracking;
import com.ilimitech.delivery.spring.ordertracking.dto.*;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
@Component
public class OrderTrackingMapper {
    public OrderTrackingDto toDto(OrderTracking e) {
        if (e == null) return null;
        OrderTrackingDto d = new OrderTrackingDto();
        d.setId(e.getId()); d.setOrderId(e.getOrderId()); d.setCourierId(e.getCourierId());
        d.setLatitude(e.getLatitude()); d.setLongitude(e.getLongitude());
        d.setStatus(e.getStatus()); d.setEstimatedArrival(e.getEstimatedArrival()); d.setCreatedAt(e.getCreatedAt());
        return d;
    }
    public OrderTracking toEntity(CreateOrderTrackingDto dto) {
        OrderTracking e = new OrderTracking();
        e.setOrderId(dto.getOrderId()); e.setCourierId(dto.getCourierId());
        e.setLatitude(dto.getLatitude()); e.setLongitude(dto.getLongitude());
        e.setStatus(dto.getStatus()); e.setEstimatedArrival(dto.getEstimatedArrival());
        e.setCreatedAt(LocalDateTime.now());
        return e;
    }
}

