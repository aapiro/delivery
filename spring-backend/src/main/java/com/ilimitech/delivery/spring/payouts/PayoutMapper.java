package com.ilimitech.delivery.spring.payouts;
import com.ilimitech.delivery.spring.payouts.dto.*;
import org.springframework.stereotype.Component;
@Component
public class PayoutMapper {
    public PayoutDto toDto(Payout e) {
        if (e == null) return null;
        PayoutDto d = new PayoutDto();
        d.setId(e.getId()); d.setRecipientType(e.getRecipientType()); d.setRecipientId(e.getRecipientId());
        d.setAmount(e.getAmount()); d.setPeriodStart(e.getPeriodStart()); d.setPeriodEnd(e.getPeriodEnd());
        d.setStatus(e.getStatus()); d.setProcessedAt(e.getProcessedAt());
        return d;
    }
    public Payout toEntity(CreatePayoutDto dto) {
        Payout e = new Payout();
        e.setRecipientType(dto.getRecipientType()); e.setRecipientId(dto.getRecipientId());
        e.setAmount(dto.getAmount()); e.setPeriodStart(dto.getPeriodStart()); e.setPeriodEnd(dto.getPeriodEnd());
        e.setStatus(dto.getStatus() != null ? dto.getStatus() : "PENDING");
        return e;
    }
}

