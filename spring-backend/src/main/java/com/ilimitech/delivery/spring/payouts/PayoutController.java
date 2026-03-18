package com.ilimitech.delivery.spring.payouts;
import com.ilimitech.delivery.spring.payouts.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/payouts")
public class PayoutController {
    private final PayoutService service;
    public PayoutController(PayoutService service) { this.service = service; }
    @GetMapping public List<PayoutDto> list() { return service.findAll(); }
    @PostMapping
    public ResponseEntity<PayoutDto> create(@Valid @RequestBody CreatePayoutDto dto) {
        if (dto.getRecipientType() == null || dto.getRecipientType().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "recipientType must not be blank");
        if (dto.getRecipientId() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "recipientId must not be null");
        if (dto.getAmount() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "amount must not be null");
        PayoutDto created = service.create(dto);
        return ResponseEntity.created(URI.create("/payouts/" + created.getId())).body(created);
    }
}

