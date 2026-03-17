package com.ilimitech.delivery.spring.couriers;

import com.ilimitech.delivery.spring.couriers.dto.CourierDto;
import com.ilimitech.delivery.spring.couriers.dto.CreateCourierDto;
import com.ilimitech.delivery.spring.couriers.dto.UpdateCourierDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourierServiceImpl implements CourierService {

    private final CourierRepository repository;
    private final CourierMapper mapper;

    public CourierServiceImpl(CourierRepository repository, CourierMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<CourierDto> findAll() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public CourierDto findById(Long id) {
        return repository.findById(id).map(mapper::toDto).orElse(null);
    }

    @Override
    public CourierDto create(CreateCourierDto dto) {
        return mapper.toDto(repository.save(mapper.toEntity(dto)));
    }

    @Override
    public CourierDto update(Long id, UpdateCourierDto dto) {
        return repository.findById(id).map(existing -> {
            mapper.applyUpdate(existing, dto);
            return mapper.toDto(repository.save(existing));
        }).orElse(null);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}

