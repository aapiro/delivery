package com.ilimitech.delivery.spring.dishreviews;

import com.ilimitech.delivery.spring.dishreviews.dto.CreateDishReviewDto;
import com.ilimitech.delivery.spring.dishreviews.dto.DishReviewDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/dish-reviews")
public class DishReviewController {

    private final DishReviewService service;

    public DishReviewController(DishReviewService service) {
        this.service = service;
    }

    @GetMapping
    public List<DishReviewDto> list() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<DishReviewDto> create(@Valid @RequestBody CreateDishReviewDto dto) {
        if (dto.getDishId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "dishId must not be null");
        }
        if (dto.getUserId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId must not be null");
        }
        if (dto.getRating() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "rating must not be null");
        }
        if (dto.getRating() < 1 || dto.getRating() > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "rating must be between 1 and 5");
        }
        DishReviewDto created = service.create(dto);
        return ResponseEntity.created(URI.create("/dish-reviews/" + created.getId())).body(created);
    }
}

