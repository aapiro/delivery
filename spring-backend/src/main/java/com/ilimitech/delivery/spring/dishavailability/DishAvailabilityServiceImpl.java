package com.ilimitech.delivery.spring.dishavailability;

import com.ilimitech.delivery.spring.dishavailability.dto.CreateDishAvailabilityDto;
import com.ilimitech.delivery.spring.dishavailability.dto.DishAvailabilityDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishAvailabilityServiceImpl implements DishAvailabilityService {

    private final DishAvailabilityRepository repository;
    private final DishAvailabilityMapper mapper;

    public DishAvailabilityServiceImpl(DishAvailabilityRepository repository, DishAvailabilityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<DishAvailabilityDto> findAll() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public DishAvailabilityDto create(CreateDishAvailabilityDto dto) {
        DishAvailability entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}

