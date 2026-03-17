package com.ilimitech.delivery.spring.courierreviews;

import com.ilimitech.delivery.spring.courierreviews.dto.CourierReviewDto;
import com.ilimitech.delivery.spring.courierreviews.dto.CreateCourierReviewDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/courier-reviews")
public class CourierReviewController {

    private final CourierReviewService service;

    public CourierReviewController(CourierReviewService service) {
        this.service = service;
    }

    @GetMapping
    public List<CourierReviewDto> list() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<CourierReviewDto> create(@Valid @RequestBody CreateCourierReviewDto dto) {
        // Manual validation as defense-in-depth
        if (dto.getCourierId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "courierId must not be null");
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
        CourierReviewDto created = service.create(dto);
        return ResponseEntity.created(URI.create("/courier-reviews/" + created.getId())).body(created);
    }
}

