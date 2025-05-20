package dev.danilo.moto2000.controller;

import dev.danilo.moto2000.dto.CategoryDTO;
import dev.danilo.moto2000.dto.Response;
import dev.danilo.moto2000.dto.ServiceOrderDTO;
import dev.danilo.moto2000.service.CategoryService;
import dev.danilo.moto2000.service.ServiceOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/api/service-orders")
@RequiredArgsConstructor
public class ServiceOrderController {

    private final ServiceOrderService service;

    @PostMapping("/save")
    public ResponseEntity<Response> saveServiceOrder(@RequestBody @Valid ServiceOrderDTO dto) {
        return ResponseEntity.ok(service.saveServiceOrder(dto));
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllServiceOrders() {
        return ResponseEntity.ok(service.getAllServiceOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getServiceOrderById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getServiceOrderById(id));
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Response> updateServiceOrder(@PathVariable UUID id, @RequestBody @Valid ServiceOrderDTO dto) {
        return ResponseEntity.ok(service.updateServiceOrder(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteServiceOrder(@PathVariable UUID id) {
        service.deleteServiceOrder(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
