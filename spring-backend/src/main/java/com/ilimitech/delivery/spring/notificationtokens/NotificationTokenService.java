package com.ilimitech.delivery.spring.notificationtokens;

import com.ilimitech.delivery.spring.notificationtokens.dto.CreateNotificationTokenDto;
import com.ilimitech.delivery.spring.notificationtokens.dto.NotificationTokenDto;
import java.util.List;

public interface NotificationTokenService {
    List<NotificationTokenDto> findAll();
    NotificationTokenDto create(CreateNotificationTokenDto dto);
}

