package com.ilimitech.delivery.spring.orderissues;
import com.ilimitech.delivery.spring.orderissues.dto.CreateOrderIssueDto;
import com.ilimitech.delivery.spring.orderissues.dto.OrderIssueDto;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/order-issues")
public class OrderIssueController {
    private final OrderIssueService service;
    public OrderIssueController(OrderIssueService service) { this.service = service; }

    @GetMapping
    public List<OrderIssueDto> list() { return service.findAll(); }

    @PostMapping
    public ResponseEntity<OrderIssueDto> create(@Valid @RequestBody CreateOrderIssueDto dto) {
        if (dto.getOrderId() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "orderId must not be null");
        OrderIssueDto created = service.create(dto);
        return ResponseEntity.created(URI.create("/order-issues/" + created.getId())).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

