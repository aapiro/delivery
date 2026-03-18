package com.ilimitech.delivery.spring.paymentmethods;
import com.ilimitech.delivery.spring.paymentmethods.dto.*;
import org.springframework.stereotype.Component;
@Component
public class PaymentMethodMapper {
    public PaymentMethodDto toDto(PaymentMethod e) {
        if (e == null) return null;
        PaymentMethodDto d = new PaymentMethodDto();
        d.setId(e.getId()); d.setUserId(e.getUserId()); d.setType(e.getType()); d.setProvider(e.getProvider());
        d.setToken(e.getToken()); d.setLastFour(e.getLastFour()); d.setIsDefault(e.getIsDefault()); d.setIsActive(e.getIsActive());
        return d;
    }
    public PaymentMethod toEntity(CreatePaymentMethodDto dto) {
        PaymentMethod e = new PaymentMethod();
        e.setUserId(dto.getUserId()); e.setType(dto.getType()); e.setProvider(dto.getProvider());
        e.setToken(dto.getToken()); e.setLastFour(dto.getLastFour());
        e.setIsDefault(dto.getIsDefault() != null ? dto.getIsDefault() : false);
        e.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        return e;
    }
}

