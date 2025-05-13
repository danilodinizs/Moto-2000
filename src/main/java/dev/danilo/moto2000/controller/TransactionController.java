package dev.danilo.moto2000.controller;

import dev.danilo.moto2000.dto.Response;
import dev.danilo.moto2000.dto.TransactionRequest;
import dev.danilo.moto2000.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService service;

    @PostMapping("/purchase")
    public ResponseEntity<Response> restockInventory(@RequestBody @Valid TransactionRequest request) {
        return ResponseEntity.ok(service.restockInventory(request));
    }

    @PostMapping("/sell")
    public ResponseEntity<Response> sell(@RequestBody @Valid TransactionRequest request) {
        return ResponseEntity.ok(service.sell(request));
    }
}
