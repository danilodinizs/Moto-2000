package dev.danilo.moto2000.controller;

import dev.danilo.moto2000.dto.Response;
import dev.danilo.moto2000.dto.TransactionRequest;
import dev.danilo.moto2000.enums.TransactionStatus;
import dev.danilo.moto2000.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @PostMapping("/return")
    public ResponseEntity<Response> returnToSupplier(@RequestBody @Valid TransactionRequest request) {
        return ResponseEntity.ok(service.returnToSupplier(request));
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000") int size,
            @RequestParam(required = false) String searchText
    ) {
        return ResponseEntity.ok(service.getAllTransactions(page, size, searchText));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getTransactionById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getTransactionById(id));
    }

    @GetMapping("/by-month-year")
    public ResponseEntity<Response> getAllTransactionByMonthAndYear(
            @RequestParam int month,
            @RequestParam int year
    ) {
        return ResponseEntity.ok(service.getAllTransactionsByMonthAndYear(month, year));
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Response> updateTransactionStatus(@PathVariable UUID id, @RequestBody @Valid TransactionStatus status) {
        return ResponseEntity.ok(service.updateTransactionStatus(id, status));
    }
}
