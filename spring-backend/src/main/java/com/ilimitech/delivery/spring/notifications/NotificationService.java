package com.ilimitech.delivery.spring.notifications;

import com.ilimitech.delivery.spring.notifications.dto.CreateNotificationDto;
import com.ilimitech.delivery.spring.notifications.dto.NotificationDto;
import java.util.List;

public interface NotificationService {
    List<NotificationDto> findAll();
    NotificationDto create(CreateNotificationDto dto);
}

