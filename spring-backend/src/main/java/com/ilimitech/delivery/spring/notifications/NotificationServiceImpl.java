package com.ilimitech.delivery.spring.notifications;

import com.ilimitech.delivery.spring.notifications.dto.CreateNotificationDto;
import com.ilimitech.delivery.spring.notifications.dto.NotificationDto;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;
    private final NotificationMapper mapper;

    public NotificationServiceImpl(NotificationRepository repository, NotificationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<NotificationDto> findAll() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public NotificationDto create(CreateNotificationDto dto) {
        return mapper.toDto(repository.save(mapper.toEntity(dto)));
    }
}

