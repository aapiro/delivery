package com.ilimitech.delivery.spring.courierreviews;

import com.ilimitech.delivery.spring.courierreviews.dto.CourierReviewDto;
import com.ilimitech.delivery.spring.courierreviews.dto.CreateCourierReviewDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourierReviewServiceImpl implements CourierReviewService {

    private final CourierReviewRepository repository;
    private final CourierReviewMapper mapper;

    public CourierReviewServiceImpl(CourierReviewRepository repository, CourierReviewMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<CourierReviewDto> findAll() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public CourierReviewDto create(CreateCourierReviewDto dto) {
        return mapper.toDto(repository.save(mapper.toEntity(dto)));
    }
}

