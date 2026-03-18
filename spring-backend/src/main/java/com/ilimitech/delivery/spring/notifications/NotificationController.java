package com.ilimitech.delivery.spring.notifications;

import com.ilimitech.delivery.spring.notifications.dto.CreateNotificationDto;
import com.ilimitech.delivery.spring.notifications.dto.NotificationDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @GetMapping
    public List<NotificationDto> list() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<NotificationDto> create(@Valid @RequestBody CreateNotificationDto dto) {
        if (dto.getUserId() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId must not be null");
        if (dto.getType() == null || dto.getType().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "type must not be blank");
        if (dto.getTitle() == null || dto.getTitle().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "title must not be blank");
        NotificationDto created = service.create(dto);
        return ResponseEntity.created(URI.create("/notifications/" + created.getId())).body(created);
    }
}

