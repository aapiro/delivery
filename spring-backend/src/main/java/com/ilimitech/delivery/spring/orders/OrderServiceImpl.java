package com.ilimitech.delivery.spring.orders;

import com.ilimitech.delivery.spring.orders.dto.CreateOrderDto;
import com.ilimitech.delivery.spring.orders.dto.OrderDto;
import com.ilimitech.delivery.spring.orders.dto.UpdateOrderDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;

    public OrderServiceImpl(OrderRepository repository, OrderMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<OrderDto> findAll() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public OrderDto findById(Long id) {
        return repository.findById(id).map(mapper::toDto).orElse(null);
    }

    @Override
    public OrderDto create(CreateOrderDto dto) {
        Order saved = repository.save(mapper.toEntity(dto));
        return mapper.toDto(saved);
    }

    @Override
    public OrderDto update(Long id, UpdateOrderDto dto) {
        return repository.findById(id).map(existing -> mapper.applyUpdate(existing, dto)).map(repository::save).map(mapper::toDto).orElse(null);
    }

    @Override
    public void delete(Long id) {
        repository.findById(id).ifPresent(repository::delete);
    }
}

