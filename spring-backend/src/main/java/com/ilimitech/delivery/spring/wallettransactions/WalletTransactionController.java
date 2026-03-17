package com.ilimitech.delivery.spring.wallettransactions;

import com.ilimitech.delivery.spring.wallettransactions.dto.CreateWalletTransactionDto;
import com.ilimitech.delivery.spring.wallettransactions.dto.UpdateWalletTransactionDto;
import com.ilimitech.delivery.spring.wallettransactions.dto.WalletTransactionDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/wallet-transactions")
public class WalletTransactionController {

    private final WalletTransactionService service;

    public WalletTransactionController(WalletTransactionService service) { this.service = service; }

    @GetMapping
    public List<WalletTransactionDto> list() { return service.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<WalletTransactionDto> getById(@PathVariable Long id) {
        WalletTransactionDto d = service.findById(id);
        return d != null ? ResponseEntity.ok(d) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<WalletTransactionDto> create(@RequestBody CreateWalletTransactionDto dto) {
        WalletTransactionDto created = service.create(dto);
        return ResponseEntity.created(URI.create("/wallet-transactions/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WalletTransactionDto> update(@PathVariable Long id, @RequestBody UpdateWalletTransactionDto dto) {
        WalletTransactionDto updated = service.update(id, dto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

