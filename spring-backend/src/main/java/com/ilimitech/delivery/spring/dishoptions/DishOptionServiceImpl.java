package com.ilimitech.delivery.spring.dishoptions;

import com.ilimitech.delivery.spring.dishoptions.dto.CreateDishOptionDto;
import com.ilimitech.delivery.spring.dishoptions.dto.DishOptionDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishOptionServiceImpl implements DishOptionService {

    private final DishOptionRepository repository;
    private final DishOptionMapper mapper;

    public DishOptionServiceImpl(DishOptionRepository repository, DishOptionMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<DishOptionDto> findAll() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public DishOptionDto create(CreateDishOptionDto dto) {
        return mapper.toDto(repository.save(mapper.toEntity(dto)));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}

