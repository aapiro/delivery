package com.ilimitech.delivery.spring.restaurants;

import com.ilimitech.delivery.spring.restaurants.dto.CreateRestaurantDto;
import com.ilimitech.delivery.spring.restaurants.dto.RestaurantDto;
import com.ilimitech.delivery.spring.restaurants.dto.UpdateRestaurantDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository repository;
    private final RestaurantMapper mapper;

    public RestaurantServiceImpl(RestaurantRepository repository, RestaurantMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<RestaurantDto> findAll() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public RestaurantDto findById(Long id) {
        return repository.findById(id).map(mapper::toDto).orElse(null);
    }

    @Override
    public RestaurantDto create(CreateRestaurantDto dto) {
        Restaurant saved = repository.save(mapper.toEntity(dto));
        return mapper.toDto(saved);
    }

    @Override
    public RestaurantDto update(Long id, UpdateRestaurantDto dto) {
        return repository.findById(id).map(existing -> mapper.applyUpdate(existing, dto)).map(repository::save).map(mapper::toDto).orElse(null);
    }

    @Override
    public void delete(Long id) {
        repository.findById(id).ifPresent(repository::delete);
    }
}

