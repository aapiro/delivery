package com.ilimitech.delivery.spring.dishoptionvalues;

import com.ilimitech.delivery.spring.dishoptionvalues.dto.CreateDishOptionValueDto;
import com.ilimitech.delivery.spring.dishoptionvalues.dto.DishOptionValueDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishOptionValueServiceImpl implements DishOptionValueService {

    private final DishOptionValueRepository repository;
    private final DishOptionValueMapper mapper;

    public DishOptionValueServiceImpl(DishOptionValueRepository repository, DishOptionValueMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<DishOptionValueDto> findAll() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public DishOptionValueDto create(CreateDishOptionValueDto dto) {
        return mapper.toDto(repository.save(mapper.toEntity(dto)));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}

