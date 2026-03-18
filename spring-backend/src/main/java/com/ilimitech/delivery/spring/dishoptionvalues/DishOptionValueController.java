package com.ilimitech.delivery.spring.dishoptionvalues;

import com.ilimitech.delivery.spring.dishoptionvalues.dto.CreateDishOptionValueDto;
import com.ilimitech.delivery.spring.dishoptionvalues.dto.DishOptionValueDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/dish-option-values")
public class DishOptionValueController {

    private final DishOptionValueService service;

    public DishOptionValueController(DishOptionValueService service) {
        this.service = service;
    }

    @GetMapping
    public List<DishOptionValueDto> list() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<DishOptionValueDto> create(@Valid @RequestBody CreateDishOptionValueDto dto) {
        if (dto.getOptionId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "optionId must not be null");
        }
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name must not be blank");
        }
        if (dto.getExtraPrice() != null && dto.getExtraPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "extraPrice must be >= 0");
        }
        DishOptionValueDto created = service.create(dto);
        return ResponseEntity.created(URI.create("/dish-option-values/" + created.getId())).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

