package com.ilimitech.delivery.spring.orderitems;

import com.ilimitech.delivery.spring.orderitems.dto.CreateOrderItemDto;
import com.ilimitech.delivery.spring.orderitems.dto.OrderItemDto;
import com.ilimitech.delivery.spring.orderitems.dto.UpdateOrderItemDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/order-items")
public class OrderItemController {

    private final OrderItemService service;

    public OrderItemController(OrderItemService service) { this.service = service; }

    @GetMapping
    public List<OrderItemDto> list() { return service.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<OrderItemDto> getById(@PathVariable Long id) {
        OrderItemDto d = service.findById(id);
        return d != null ? ResponseEntity.ok(d) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<OrderItemDto> create(@RequestBody CreateOrderItemDto dto) {
        OrderItemDto created = service.create(dto);
        return ResponseEntity.created(URI.create("/order-items/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderItemDto> update(@PathVariable Long id, @RequestBody UpdateOrderItemDto dto) {
        OrderItemDto updated = service.update(id, dto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

