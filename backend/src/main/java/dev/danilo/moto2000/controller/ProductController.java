package dev.danilo.moto2000.controller;

import dev.danilo.moto2000.dto.CategoryDTO;
import dev.danilo.moto2000.dto.ProductDTO;
import dev.danilo.moto2000.dto.Response;
import dev.danilo.moto2000.service.CategoryService;
import dev.danilo.moto2000.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/v1/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<Response> saveProduct(
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam("name") String name,
            @RequestParam(value = "sku", required = false) String sku,
            @RequestParam("price") BigDecimal price,
            @RequestParam("stockQuantity") Integer stockQuantity,
            @RequestParam("categoryId") UUID categoryId,
            @RequestParam("supplierId") UUID supplierId,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "expiryDate", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate expiryDate
    ) {

        ProductDTO dto = ProductDTO.builder()
                .name(name)
                .sku(sku)
                .price(price)
                .stockQuantity(stockQuantity)
                .categoryId(categoryId)
                .supplierId(supplierId)
                .description(description)
                .expiryDate(expiryDate)
                .build();

        return ResponseEntity.ok(service.saveProduct(dto, imageFile));
    }

    @PatchMapping("/update")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<Response> updateProduct(
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam("name") String name,
            @RequestParam(value = "sku", required = false) String sku,
            @RequestParam("price") BigDecimal price,
            @RequestParam("stockQuantity") Integer stockQuantity,
            @RequestParam("categoryId") UUID categoryId,
            @RequestParam("productId") UUID productId,
            @RequestParam("supplierId") UUID supplierId,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "expiryDate", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate expiryDate
    ) {


        ProductDTO dto = ProductDTO.builder()
                .name(name)
                .sku(sku)
                .price(price)
                .stockQuantity(stockQuantity)
                .categoryId(categoryId)
                .productId(productId)
                .supplierId(supplierId)
                .description(description)
                .expiryDate(expiryDate)
                .build();

        return ResponseEntity.ok(service.updateProduct(dto, imageFile));
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllProducts() {
        return ResponseEntity.ok(service.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getProductById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getProductById(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        service.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
