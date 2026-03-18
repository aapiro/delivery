package com.ilimitech.delivery.spring.ordertracking;
import com.ilimitech.delivery.spring.ordertracking.dto.*;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class OrderTrackingService {
    private final OrderTrackingRepository repo;
    private final OrderTrackingMapper mapper;
    public OrderTrackingService(OrderTrackingRepository repo, OrderTrackingMapper mapper) { this.repo = repo; this.mapper = mapper; }
    public List<OrderTrackingDto> findAll() { return repo.findAll().stream().map(mapper::toDto).collect(Collectors.toList()); }
    public OrderTrackingDto create(CreateOrderTrackingDto dto) { return mapper.toDto(repo.save(mapper.toEntity(dto))); }
}

