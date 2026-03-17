package com.ilimitech.delivery.spring.addresses;

import com.ilimitech.delivery.spring.addresses.dto.AddressDto;
import com.ilimitech.delivery.spring.addresses.dto.CreateAddressDto;
import com.ilimitech.delivery.spring.addresses.dto.UpdateAddressDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    private final AddressService service;

    public AddressController(AddressService service) {
        this.service = service;
    }

    @GetMapping
    public List<AddressDto> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressDto> getById(@PathVariable Long id) {
        AddressDto dto = service.findById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<AddressDto> create(@RequestBody CreateAddressDto dto) {
        AddressDto created = service.create(dto);
        return ResponseEntity.created(URI.create("/addresses/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressDto> update(@PathVariable Long id, @RequestBody UpdateAddressDto dto) {
        AddressDto updated = service.update(id, dto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

