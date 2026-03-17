package com.ilimitech.delivery.spring.auditlogs;

import com.ilimitech.delivery.spring.auditlogs.dto.AuditLogDto;
import org.springframework.stereotype.Component;

@Component
public class AuditLogMapper {

    public AuditLogDto toDto(AuditLog e) {
        if (e == null) return null;
        AuditLogDto dto = new AuditLogDto();
        dto.setId(e.getId());
        dto.setTableName(e.getTableName());
        dto.setRecordId(e.getRecordId());
        dto.setAction(e.getAction());
        dto.setOldData(e.getOldData());
        dto.setNewData(e.getNewData());
        dto.setCreatedAt(e.getCreatedAt());
        return dto;
    }
}

