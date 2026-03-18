package com.ilimitech.delivery.spring.ordertracking;
import com.ilimitech.delivery.spring.ordertracking.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.net.URI;
import java.util.List;
@RestController
@RequestMapping("/order-tracking")
public class OrderTrackingController {
    private final OrderTrackingService service;
    public OrderTrackingController(OrderTrackingService service) { this.service = service; }
    @GetMapping public List<OrderTrackingDto> list() { return service.findAll(); }
    @PostMapping
    public ResponseEntity<OrderTrackingDto> create(@Valid @RequestBody CreateOrderTrackingDto dto) {
        if (dto.getOrderId() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "orderId must not be null");
        OrderTrackingDto created = service.create(dto);
        return ResponseEntity.created(URI.create("/order-tracking/" + created.getId())).body(created);
    }
}

