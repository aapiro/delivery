package com.ilimitech.delivery.spring.referralcodes;
import com.ilimitech.delivery.spring.referralcodes.dto.*;
import org.springframework.stereotype.Component;
@Component
public class ReferralCodeMapper {
    public ReferralCodeDto toDto(ReferralCode e) {
        if (e == null) return null;
        ReferralCodeDto d = new ReferralCodeDto();
        d.setId(e.getId()); d.setUserId(e.getUserId()); d.setCode(e.getCode());
        d.setDiscountAmount(e.getDiscountAmount()); d.setTimesUsed(e.getTimesUsed()); d.setMaxUses(e.getMaxUses());
        return d;
    }
    public ReferralCode toEntity(CreateReferralCodeDto dto) {
        ReferralCode e = new ReferralCode();
        e.setUserId(dto.getUserId()); e.setCode(dto.getCode());
        e.setDiscountAmount(dto.getDiscountAmount()); e.setMaxUses(dto.getMaxUses()); e.setTimesUsed(0);
        return e;
    }
    public ReferralCode applyUpdate(ReferralCode e, UpdateReferralCodeDto dto) {
        if (dto.getCode() != null) e.setCode(dto.getCode());
        if (dto.getDiscountAmount() != null) e.setDiscountAmount(dto.getDiscountAmount());
        if (dto.getTimesUsed() != null) e.setTimesUsed(dto.getTimesUsed());
        if (dto.getMaxUses() != null) e.setMaxUses(dto.getMaxUses());
        return e;
    }
}

