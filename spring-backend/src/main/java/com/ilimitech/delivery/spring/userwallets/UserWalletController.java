package com.ilimitech.delivery.spring.userwallets;

import com.ilimitech.delivery.spring.userwallets.dto.CreateUserWalletDto;
import com.ilimitech.delivery.spring.userwallets.dto.UpdateUserWalletDto;
import com.ilimitech.delivery.spring.userwallets.dto.UserWalletDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/user-wallets")
public class UserWalletController {

    private final UserWalletService service;

    public UserWalletController(UserWalletService service) { this.service = service; }

    @GetMapping
    public List<UserWalletDto> list() { return service.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<UserWalletDto> getById(@PathVariable Long id) {
        UserWalletDto d = service.findById(id);
        return d != null ? ResponseEntity.ok(d) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<UserWalletDto> create(@RequestBody CreateUserWalletDto dto) {
        UserWalletDto created = service.create(dto);
        return ResponseEntity.created(URI.create("/user-wallets/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserWalletDto> update(@PathVariable Long id, @RequestBody UpdateUserWalletDto dto) {
        UserWalletDto updated = service.update(id, dto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

