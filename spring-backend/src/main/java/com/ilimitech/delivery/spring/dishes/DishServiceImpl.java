package com.ilimitech.delivery.spring.dishes;

import com.ilimitech.delivery.spring.dishes.dto.CreateDishDto;
import com.ilimitech.delivery.spring.dishes.dto.DishDto;
import com.ilimitech.delivery.spring.dishes.dto.UpdateDishDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl implements DishService {
    private final DishRepository repository;
    private final DishMapper mapper;

    public DishServiceImpl(DishRepository repository, DishMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<DishDto> findAll() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public DishDto findById(Long id) {
        return repository.findById(id).map(mapper::toDto).orElse(null);
    }

    @Override
    public DishDto create(CreateDishDto dto) {
        Dish entity = mapper.toEntity(dto);
        Dish saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    public DishDto update(Long id, UpdateDishDto dto) {
        return repository.findById(id).map(existing -> {
            Dish updated = mapper.applyUpdate(existing, dto);
            return mapper.toDto(repository.save(updated));
        }).orElse(null);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}

