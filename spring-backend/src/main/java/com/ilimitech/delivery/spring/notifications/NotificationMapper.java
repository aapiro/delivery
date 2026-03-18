package com.ilimitech.delivery.spring.notifications;

import com.ilimitech.delivery.spring.notifications.dto.CreateNotificationDto;
import com.ilimitech.delivery.spring.notifications.dto.NotificationDto;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class NotificationMapper {

    public NotificationDto toDto(Notification e) {
        if (e == null) return null;
        NotificationDto dto = new NotificationDto();
        dto.setId(e.getId());
        dto.setUserId(e.getUserId());
        dto.setType(e.getType());
        dto.setTitle(e.getTitle());
        dto.setMessage(e.getMessage());
        dto.setData(e.getData());
        dto.setIsRead(e.getIsRead());
        dto.setCreatedAt(e.getCreatedAt());
        return dto;
    }

    public Notification toEntity(CreateNotificationDto dto) {
        if (dto == null) return null;
        Notification e = new Notification();
        e.setUserId(dto.getUserId());
        e.setType(dto.getType());
        e.setTitle(dto.getTitle());
        e.setMessage(dto.getMessage());
        e.setData(dto.getData());
        e.setIsRead(dto.getIsRead() != null ? dto.getIsRead() : false);
        e.setCreatedAt(LocalDateTime.now());
        return e;
    }
}

