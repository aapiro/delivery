package com.ilimitech.delivery.spring.auditlogs;

import com.ilimitech.delivery.spring.auditlogs.dto.AuditLogDto;

import java.util.List;

public interface AuditLogService {
    List<AuditLogDto> findAll();
}

