package com.ilimitech.delivery.spring.orderstatushistory;
import com.ilimitech.delivery.spring.orderstatushistory.dto.*;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class OrderStatusHistoryService {
    private final OrderStatusHistoryRepository repo;
    private final OrderStatusHistoryMapper mapper;
    public OrderStatusHistoryService(OrderStatusHistoryRepository repo, OrderStatusHistoryMapper mapper) { this.repo = repo; this.mapper = mapper; }
    public List<OrderStatusHistoryDto> findAll() { return repo.findAll().stream().map(mapper::toDto).collect(Collectors.toList()); }
    public OrderStatusHistoryDto create(CreateOrderStatusHistoryDto dto) { return mapper.toDto(repo.save(mapper.toEntity(dto))); }
}

