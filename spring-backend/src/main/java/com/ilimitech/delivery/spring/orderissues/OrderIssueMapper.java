package com.ilimitech.delivery.spring.orderissues;
import com.ilimitech.delivery.spring.orderissues.dto.CreateOrderIssueDto;
import com.ilimitech.delivery.spring.orderissues.dto.OrderIssueDto;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class OrderIssueMapper {
    public OrderIssueDto toDto(OrderIssue e) {
        if (e == null) return null;
        OrderIssueDto d = new OrderIssueDto();
        d.setId(e.getId()); d.setOrderId(e.getOrderId()); d.setType(e.getType());
        d.setDescription(e.getDescription()); d.setCreatedAt(e.getCreatedAt());
        return d;
    }
    public OrderIssue toEntity(CreateOrderIssueDto dto) {
        if (dto == null) return null;
        OrderIssue e = new OrderIssue();
        e.setOrderId(dto.getOrderId()); e.setType(dto.getType());
        e.setDescription(dto.getDescription()); e.setCreatedAt(LocalDateTime.now());
        return e;
    }
}

