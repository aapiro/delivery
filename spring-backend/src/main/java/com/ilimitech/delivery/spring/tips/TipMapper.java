package com.ilimitech.delivery.spring.tips;

import com.ilimitech.delivery.spring.tips.dto.CreateTipDto;
import com.ilimitech.delivery.spring.tips.dto.TipDto;
import com.ilimitech.delivery.spring.tips.dto.UpdateTipDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class TipMapper {

    public TipDto toDto(Tip t){
        if (t == null) return null;
        TipDto d = new TipDto();
        d.setId(t.getId());
        d.setOrderId(t.getOrderId());
        d.setCourierId(t.getCourierId());
        d.setAmount(t.getAmount());
        d.setTipType(t.getTipType());
        d.setCreatedAt(t.getCreatedAt());
        return d;
    }

    public Tip toEntity(CreateTipDto d){
        if (d == null) return null;
        Tip t = new Tip();
        t.setOrderId(d.getOrderId());
        t.setCourierId(d.getCourierId());
        t.setAmount(d.getAmount());
        t.setTipType(d.getTipType());
        t.setCreatedAt(LocalDateTime.now());
        return t;
    }

    public Tip applyUpdate(Tip existing, UpdateTipDto d){
        Optional.ofNullable(d.getOrderId()).ifPresent(existing::setOrderId);
        Optional.ofNullable(d.getCourierId()).ifPresent(existing::setCourierId);
        Optional.ofNullable(d.getAmount()).ifPresent(existing::setAmount);
        Optional.ofNullable(d.getTipType()).ifPresent(existing::setTipType);
        return existing;
    }
}

