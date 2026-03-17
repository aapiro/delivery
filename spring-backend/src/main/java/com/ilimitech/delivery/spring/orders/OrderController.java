package com.ilimitech.delivery.spring.orders;

import com.ilimitech.delivery.spring.orders.dto.CreateOrderDto;
import com.ilimitech.delivery.spring.orders.dto.OrderDto;
import com.ilimitech.delivery.spring.orders.dto.UpdateOrderDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) { this.service = service; }

    @GetMapping
    public List<OrderDto> list() { return service.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getById(@PathVariable Long id) {
        OrderDto d = service.findById(id);
        return d != null ? ResponseEntity.ok(d) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<OrderDto> create(@RequestBody CreateOrderDto dto) {
        OrderDto created = service.create(dto);
        return ResponseEntity.created(URI.create("/orders/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDto> update(@PathVariable Long id, @RequestBody UpdateOrderDto dto) {
        OrderDto updated = service.update(id, dto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

