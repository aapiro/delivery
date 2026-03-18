package com.ilimitech.delivery.spring.notificationtokens;

import com.ilimitech.delivery.spring.notificationtokens.dto.CreateNotificationTokenDto;
import com.ilimitech.delivery.spring.notificationtokens.dto.NotificationTokenDto;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationTokenServiceImpl implements NotificationTokenService {

    private final NotificationTokenRepository repository;
    private final NotificationTokenMapper mapper;

    public NotificationTokenServiceImpl(NotificationTokenRepository repository, NotificationTokenMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<NotificationTokenDto> findAll() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public NotificationTokenDto create(CreateNotificationTokenDto dto) {
        return mapper.toDto(repository.save(mapper.toEntity(dto)));
    }
}

