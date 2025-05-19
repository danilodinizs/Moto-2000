package dev.danilo.moto2000.controller;

import dev.danilo.moto2000.dto.CategoryDTO;
import dev.danilo.moto2000.dto.ClientDTO;
import dev.danilo.moto2000.dto.Response;
import dev.danilo.moto2000.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService service;

    @PostMapping("/save")
    public ResponseEntity<Response> createClient(@RequestBody @Valid ClientDTO dto) {
        return ResponseEntity.ok(service.saveClient(dto));
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllClients() {
        return ResponseEntity.ok(service.getAllClients());
    }

    @DeleteMapping("/transactions/{id}")
    public ResponseEntity<Response> getClientTransactions(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getTransactions(id));
    }


    @PatchMapping("/update/{id}")
    public ResponseEntity<Response> updateClient(@PathVariable UUID id, @RequestBody @Valid ClientDTO dto) {
        return ResponseEntity.ok(service.updateClient(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable UUID id) {
        service.deleteClient(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
