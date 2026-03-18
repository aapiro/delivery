package com.ilimitech.delivery.spring.paymentmethods;
import com.ilimitech.delivery.spring.paymentmethods.dto.*;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class PaymentMethodService {
    private final PaymentMethodRepository repo; private final PaymentMethodMapper mapper;
    public PaymentMethodService(PaymentMethodRepository repo, PaymentMethodMapper mapper) { this.repo = repo; this.mapper = mapper; }
    public List<PaymentMethodDto> findAll() { return repo.findAll().stream().map(mapper::toDto).collect(Collectors.toList()); }
    public PaymentMethodDto create(CreatePaymentMethodDto dto) { return mapper.toDto(repo.save(mapper.toEntity(dto))); }
    public void delete(Long id) { repo.deleteById(id); }
}

