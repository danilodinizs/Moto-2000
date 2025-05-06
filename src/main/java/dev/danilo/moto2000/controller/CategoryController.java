package dev.danilo.moto2000.controller;

import dev.danilo.moto2000.dto.CategoryDTO;
import dev.danilo.moto2000.dto.Response;
import dev.danilo.moto2000.dto.UserDTO;
import dev.danilo.moto2000.entity.User;
import dev.danilo.moto2000.service.CategoryService;
import dev.danilo.moto2000.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @PostMapping("/save")
    public ResponseEntity<Response> createCategory(@RequestBody @Valid CategoryDTO dto) {
        return ResponseEntity.ok(service.createCategory(dto));
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllCategories() {
        return ResponseEntity.ok(service.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getCategoryById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getCategoryById(id));
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Response> updateCategory(@PathVariable UUID id, @RequestBody @Valid CategoryDTO dto) {
        return ResponseEntity.ok(service.updateCategory(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteCategory(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(service.deleteCategory(id));
    }

}
