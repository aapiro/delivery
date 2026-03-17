package com.ilimitech.delivery.spring.restaurants;

import com.ilimitech.delivery.spring.restaurants.dto.CreateRestaurantDto;
import com.ilimitech.delivery.spring.restaurants.dto.RestaurantDto;
import com.ilimitech.delivery.spring.restaurants.dto.UpdateRestaurantDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantService service;

    public RestaurantController(RestaurantService service) {
        this.service = service;
    }

    @GetMapping
    public List<RestaurantDto> list() { return service.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDto> getById(@PathVariable Long id) {
        RestaurantDto dto = service.findById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<RestaurantDto> create(@RequestBody CreateRestaurantDto dto) {
        RestaurantDto created = service.create(dto);
        return ResponseEntity.created(URI.create("/restaurants/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantDto> update(@PathVariable Long id, @RequestBody UpdateRestaurantDto dto) {
        RestaurantDto updated = service.update(id, dto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

