package com.ilimitech.delivery.spring.dishes;

import com.ilimitech.delivery.spring.dishes.dto.CreateDishDto;
import com.ilimitech.delivery.spring.dishes.dto.DishDto;
import com.ilimitech.delivery.spring.dishes.dto.UpdateDishDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/dishes")
public class DishController {

    private final DishService service;

    public DishController(DishService service) {
        this.service = service;
    }

    @GetMapping
    public List<DishDto> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DishDto> getById(@PathVariable Long id) {
        DishDto dto = service.findById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<DishDto> create(@Valid @RequestBody CreateDishDto dto) {
        // Manual validation as defense-in-depth (in case @Valid provider is absent)
        List<String> errors = new ArrayList<>();
        if (dto.getRestaurantId() == null) {
            errors.add("restaurantId must not be null");
        }
        if (dto.getName() == null || dto.getName().isBlank()) {
            errors.add("name must not be blank");
        }
        if (dto.getPrice() == null) {
            errors.add("price must not be null");
        } else if (dto.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add("price must be greater than 0");
        }
        if (!errors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join("; ", errors));
        }
        DishDto created = service.create(dto);
        return ResponseEntity.created(URI.create("/dishes/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DishDto> update(@PathVariable Long id, @Valid @RequestBody UpdateDishDto dto) {
        // Manual validation: name, if provided, must not be blank
        if (dto.getName() != null && dto.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name must not be blank");
        }
        // Manual validation: price, if provided, must be > 0
        if (dto.getPrice() != null && dto.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "price must be greater than 0");
        }
        DishDto updated = service.update(id, dto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

