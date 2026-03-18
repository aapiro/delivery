package com.ilimitech.delivery.spring.paymentmethods;
import com.ilimitech.delivery.spring.paymentmethods.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.net.URI; import java.util.List;
@RestController @RequestMapping("/payment-methods")
public class PaymentMethodController {
    private final PaymentMethodService service;
    public PaymentMethodController(PaymentMethodService service) { this.service = service; }
    @GetMapping public List<PaymentMethodDto> list() { return service.findAll(); }
    @PostMapping
    public ResponseEntity<PaymentMethodDto> create(@Valid @RequestBody CreatePaymentMethodDto dto) {
        if (dto.getUserId() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId must not be null");
        if (dto.getType() == null || dto.getType().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "type must not be blank");
        PaymentMethodDto created = service.create(dto);
        return ResponseEntity.created(URI.create("/payment-methods/" + created.getId())).body(created);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { service.delete(id); return ResponseEntity.noContent().build(); }
}

