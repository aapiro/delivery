package com.ilimitech.delivery.spring.orderitems;

import com.ilimitech.delivery.spring.orderitems.dto.CreateOrderItemDto;
import com.ilimitech.delivery.spring.orderitems.dto.OrderItemDto;
import com.ilimitech.delivery.spring.orderitems.dto.UpdateOrderItemDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository repository;
    private final OrderItemMapper mapper;

    public OrderItemServiceImpl(OrderItemRepository repository, OrderItemMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<OrderItemDto> findAll() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public OrderItemDto findById(Long id) {
        return repository.findById(id).map(mapper::toDto).orElse(null);
    }

    @Override
    public OrderItemDto create(CreateOrderItemDto dto) {
        OrderItem saved = repository.save(mapper.toEntity(dto));
        return mapper.toDto(saved);
    }

    @Override
    public OrderItemDto update(Long id, UpdateOrderItemDto dto) {
        return repository.findById(id).map(existing -> mapper.applyUpdate(existing, dto)).map(repository::save).map(mapper::toDto).orElse(null);
    }

    @Override
    public void delete(Long id) {
        repository.findById(id).ifPresent(repository::delete);
    }
}

