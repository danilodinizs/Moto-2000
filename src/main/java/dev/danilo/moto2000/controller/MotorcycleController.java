package dev.danilo.moto2000.controller;

import dev.danilo.moto2000.dto.MotorcycleDTO;
import dev.danilo.moto2000.dto.Response;
import dev.danilo.moto2000.service.MotorcycleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/api/motorcycles")
@RequiredArgsConstructor
public class MotorcycleController {

    private final MotorcycleService service;

    @PostMapping("/save")
    public ResponseEntity<Response> createMotorcycle(@RequestBody @Valid MotorcycleDTO dto) {
        return ResponseEntity.ok(service.saveMotorcycle(dto));
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllMotorcycles() {
        return ResponseEntity.ok(service.getAllMotorcycle());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getMotorcycleById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getMotorcycleById(id));
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Response> updateMotorcycle(@PathVariable UUID id, @RequestBody @Valid MotorcycleDTO dto) {
        return ResponseEntity.ok(service.updateMotorcycle(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteMotorcycle(@PathVariable UUID id) {
        service.deleteMotorcycle(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
