package com.ilimitech.delivery.spring.orderstatushistory;
import com.ilimitech.delivery.spring.orderstatushistory.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.net.URI;
import java.util.List;
@RestController
@RequestMapping("/order-status-history")
public class OrderStatusHistoryController {
    private final OrderStatusHistoryService service;
    public OrderStatusHistoryController(OrderStatusHistoryService service) { this.service = service; }
    @GetMapping public List<OrderStatusHistoryDto> list() { return service.findAll(); }
    @PostMapping
    public ResponseEntity<OrderStatusHistoryDto> create(@Valid @RequestBody CreateOrderStatusHistoryDto dto) {
        if (dto.getOrderId() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "orderId must not be null");
        if (dto.getStatus() == null || dto.getStatus().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "status must not be blank");
        OrderStatusHistoryDto created = service.create(dto);
        return ResponseEntity.created(URI.create("/order-status-history/" + created.getId())).body(created);
    }
}

