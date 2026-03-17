package com.ilimitech.delivery.spring.couriers;

import com.ilimitech.delivery.spring.couriers.dto.CourierDto;
import com.ilimitech.delivery.spring.couriers.dto.CreateCourierDto;
import com.ilimitech.delivery.spring.couriers.dto.UpdateCourierDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/couriers")
public class CourierController {

    private final CourierService service;

    public CourierController(CourierService service) {
        this.service = service;
    }

    @GetMapping
    public List<CourierDto> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourierDto> getById(@PathVariable Long id) {
        CourierDto dto = service.findById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<CourierDto> create(@Valid @RequestBody CreateCourierDto dto) {
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name must not be blank");
        }
        CourierDto created = service.create(dto);
        return ResponseEntity.created(URI.create("/couriers/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourierDto> update(@PathVariable Long id, @Valid @RequestBody UpdateCourierDto dto) {
        if (dto.getName() != null && dto.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name must not be blank");
        }
        CourierDto updated = service.update(id, dto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

