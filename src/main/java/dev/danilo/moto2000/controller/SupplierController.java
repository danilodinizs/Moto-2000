package dev.danilo.moto2000.controller;

import dev.danilo.moto2000.dto.CategoryDTO;
import dev.danilo.moto2000.dto.Response;
import dev.danilo.moto2000.dto.SupplierDTO;
import dev.danilo.moto2000.entity.Supplier;
import dev.danilo.moto2000.service.CategoryService;
import dev.danilo.moto2000.service.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService service;

    @PostMapping("/save")
    public ResponseEntity<Response> createSupplier(@RequestBody @Valid SupplierDTO dto) {
        return ResponseEntity.ok(service.addSupplier(dto));
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllSuppliers() {
        return ResponseEntity.ok(service.getAllSuppliers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getSupplierById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getSupplierById(id));
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Response> updateSupplier(@PathVariable UUID id, @RequestBody @Valid SupplierDTO dto) {
        return ResponseEntity.ok(service.updateSupplier(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable UUID id) {
        service.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }

}
