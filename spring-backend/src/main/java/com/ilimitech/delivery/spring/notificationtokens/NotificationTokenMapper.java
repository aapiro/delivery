package com.ilimitech.delivery.spring.notificationtokens;

import com.ilimitech.delivery.spring.notificationtokens.dto.CreateNotificationTokenDto;
import com.ilimitech.delivery.spring.notificationtokens.dto.NotificationTokenDto;
import org.springframework.stereotype.Component;

@Component
public class NotificationTokenMapper {

    public NotificationTokenDto toDto(NotificationToken e) {
        if (e == null) return null;
        NotificationTokenDto dto = new NotificationTokenDto();
        dto.setId(e.getId());
        dto.setUserId(e.getUserId());
        dto.setToken(e.getToken());
        dto.setPlatform(e.getPlatform());
        dto.setIsActive(e.getIsActive());
        return dto;
    }

    public NotificationToken toEntity(CreateNotificationTokenDto dto) {
        if (dto == null) return null;
        NotificationToken e = new NotificationToken();
        e.setUserId(dto.getUserId());
        e.setToken(dto.getToken());
        e.setPlatform(dto.getPlatform());
        e.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        return e;
    }
}

