package com.ilimitech.delivery.spring.auditlogs;

import com.ilimitech.delivery.spring.auditlogs.dto.AuditLogDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/audit-logs")
public class AuditLogController {

    private final AuditLogService service;

    public AuditLogController(AuditLogService service) {
        this.service = service;
    }

    @GetMapping
    public List<AuditLogDto> list() {
        return service.findAll();
    }
}

