package com.ilimitech.delivery.spring.transactions;

import com.ilimitech.delivery.spring.transactions.dto.CreateTransactionDto;
import com.ilimitech.delivery.spring.transactions.dto.TransactionDto;
import com.ilimitech.delivery.spring.transactions.dto.UpdateTransactionDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @GetMapping
    public List<TransactionDto> list() { return service.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDto> getById(@PathVariable Long id) {
        TransactionDto dto = service.findById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<TransactionDto> create(@RequestBody CreateTransactionDto dto) {
        TransactionDto created = service.create(dto);
        return ResponseEntity.created(URI.create("/transactions/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionDto> update(@PathVariable Long id, @RequestBody UpdateTransactionDto dto) {
        TransactionDto updated = service.update(id, dto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

