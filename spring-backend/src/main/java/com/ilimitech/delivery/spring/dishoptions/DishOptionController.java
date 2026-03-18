package com.ilimitech.delivery.spring.dishoptions;

import com.ilimitech.delivery.spring.dishoptions.dto.CreateDishOptionDto;
import com.ilimitech.delivery.spring.dishoptions.dto.DishOptionDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/dish-options")
public class DishOptionController {

    private final DishOptionService service;

    public DishOptionController(DishOptionService service) {
        this.service = service;
    }

    @GetMapping
    public List<DishOptionDto> list() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<DishOptionDto> create(@Valid @RequestBody CreateDishOptionDto dto) {
        if (dto.getDishId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "dishId must not be null");
        }
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name must not be blank");
        }
        DishOptionDto created = service.create(dto);
        return ResponseEntity.created(URI.create("/dish-options/" + created.getId())).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

