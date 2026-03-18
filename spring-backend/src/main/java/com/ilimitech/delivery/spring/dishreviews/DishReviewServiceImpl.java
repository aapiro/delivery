package com.ilimitech.delivery.spring.dishreviews;

import com.ilimitech.delivery.spring.dishreviews.dto.CreateDishReviewDto;
import com.ilimitech.delivery.spring.dishreviews.dto.DishReviewDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishReviewServiceImpl implements DishReviewService {

    private final DishReviewRepository repository;
    private final DishReviewMapper mapper;

    public DishReviewServiceImpl(DishReviewRepository repository, DishReviewMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<DishReviewDto> findAll() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public DishReviewDto create(CreateDishReviewDto dto) {
        return mapper.toDto(repository.save(mapper.toEntity(dto)));
    }
}

