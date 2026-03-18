package com.ilimitech.delivery.spring.notificationtokens;

import com.ilimitech.delivery.spring.notificationtokens.dto.CreateNotificationTokenDto;
import com.ilimitech.delivery.spring.notificationtokens.dto.NotificationTokenDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/notification-tokens")
public class NotificationTokenController {

    private final NotificationTokenService service;

    public NotificationTokenController(NotificationTokenService service) {
        this.service = service;
    }

    @GetMapping
    public List<NotificationTokenDto> list() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<NotificationTokenDto> create(@Valid @RequestBody CreateNotificationTokenDto dto) {
        if (dto.getUserId() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId must not be null");
        if (dto.getToken() == null || dto.getToken().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "token must not be blank");
        NotificationTokenDto created = service.create(dto);
        return ResponseEntity.created(URI.create("/notification-tokens/" + created.getId())).body(created);
    }
}

