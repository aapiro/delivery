package com.ilimitech.delivery.spring.dishavailability;

import com.ilimitech.delivery.spring.dishavailability.dto.CreateDishAvailabilityDto;
import com.ilimitech.delivery.spring.dishavailability.dto.DishAvailabilityDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/dish-availability")
public class DishAvailabilityController {

    private final DishAvailabilityService service;

    public DishAvailabilityController(DishAvailabilityService service) {
        this.service = service;
    }

    @GetMapping
    public List<DishAvailabilityDto> list() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<DishAvailabilityDto> create(@Valid @RequestBody CreateDishAvailabilityDto dto) {
        // Manual validation as defense-in-depth
        if (dto.getDishId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "dishId must not be null");
        }
        if (dto.getDayOfWeek() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "dayOfWeek must not be null");
        }
        if (dto.getDayOfWeek() < 0 || dto.getDayOfWeek() > 6) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "dayOfWeek must be between 0 and 6");
        }
        DishAvailabilityDto created = service.create(dto);
        return ResponseEntity.created(URI.create("/dish-availability/" + created.getId())).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

