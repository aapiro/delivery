package com.ilimitech.delivery.spring.menucategories;

import com.ilimitech.delivery.spring.menucategories.dto.CreateMenuCategoryDto;
import com.ilimitech.delivery.spring.menucategories.dto.MenuCategoryDto;
import com.ilimitech.delivery.spring.menucategories.dto.UpdateMenuCategoryDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/menu-categories")
public class MenuCategoryController {

    private final MenuCategoryService service;

    public MenuCategoryController(MenuCategoryService service) {
        this.service = service;
    }

    @GetMapping
    public List<MenuCategoryDto> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuCategoryDto> getById(@PathVariable Long id) {
        MenuCategoryDto dto = service.findById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<MenuCategoryDto> create(@Valid @RequestBody CreateMenuCategoryDto dto) {
        if (dto.getRestaurantId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "restaurantId must not be null");
        }
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name must not be blank");
        }
        if (dto.getSlug() == null || dto.getSlug().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "slug must not be blank");
        }
        MenuCategoryDto created = service.create(dto);
        return ResponseEntity.created(URI.create("/menu-categories/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuCategoryDto> update(@PathVariable Long id, @Valid @RequestBody UpdateMenuCategoryDto dto) {
        if (dto.getName() != null && dto.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name must not be blank");
        }
        MenuCategoryDto updated = service.update(id, dto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

