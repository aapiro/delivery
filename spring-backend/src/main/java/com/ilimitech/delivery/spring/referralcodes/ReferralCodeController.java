package com.ilimitech.delivery.spring.referralcodes;
import com.ilimitech.delivery.spring.referralcodes.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.net.URI;
import java.util.List;
@RestController @RequestMapping("/referral-codes")
public class ReferralCodeController {
    private final ReferralCodeService service;
    public ReferralCodeController(ReferralCodeService service) { this.service = service; }

    @GetMapping
    public List<ReferralCodeDto> list() { return service.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<ReferralCodeDto> getById(@PathVariable Long id) {
        ReferralCodeDto dto = service.findById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<ReferralCodeDto> create(@Valid @RequestBody CreateReferralCodeDto dto) {
        if (dto.getUserId() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId must not be null");
        if (dto.getCode() == null || dto.getCode().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "code must not be blank");
        ReferralCodeDto created = service.create(dto);
        return ResponseEntity.created(URI.create("/referral-codes/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReferralCodeDto> update(@PathVariable Long id, @RequestBody UpdateReferralCodeDto dto) {
        ReferralCodeDto updated = service.update(id, dto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
