package com.ilimitech.delivery.spring.payouts;
import com.ilimitech.delivery.spring.payouts.dto.*;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class PayoutService {
    private final PayoutRepository repo; private final PayoutMapper mapper;
    public PayoutService(PayoutRepository repo, PayoutMapper mapper) { this.repo = repo; this.mapper = mapper; }
    public List<PayoutDto> findAll() { return repo.findAll().stream().map(mapper::toDto).collect(Collectors.toList()); }
    public PayoutDto create(CreatePayoutDto dto) { return mapper.toDto(repo.save(mapper.toEntity(dto))); }
}

