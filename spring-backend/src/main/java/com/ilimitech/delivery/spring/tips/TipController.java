package com.ilimitech.delivery.spring.tips;

import com.ilimitech.delivery.spring.tips.dto.CreateTipDto;
import com.ilimitech.delivery.spring.tips.dto.TipDto;
import com.ilimitech.delivery.spring.tips.dto.UpdateTipDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tips")
public class TipController {

    private final TipService service;

    public TipController(TipService service) { this.service = service; }

    @GetMapping
    public List<TipDto> list() { return service.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<TipDto> getById(@PathVariable Long id) {
        TipDto d = service.findById(id);
        return d != null ? ResponseEntity.ok(d) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<TipDto> create(@RequestBody CreateTipDto dto) {
        TipDto created = service.create(dto);
        return ResponseEntity.created(URI.create("/tips/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipDto> update(@PathVariable Long id, @RequestBody UpdateTipDto dto) {
        TipDto updated = service.update(id, dto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

