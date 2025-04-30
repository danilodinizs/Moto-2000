package dev.danilo.moto2000.controller;

import dev.danilo.moto2000.dto.LoginRequest;
import dev.danilo.moto2000.dto.RegisterRequest;
import dev.danilo.moto2000.dto.Response;
import dev.danilo.moto2000.dto.UserDTO;
import dev.danilo.moto2000.entity.User;
import dev.danilo.moto2000.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PreAuthorize("hasAuthority('MANAGER')")
    @GetMapping("/all")
    public ResponseEntity<Response> getAllUsers() {
        return ResponseEntity.ok(service.getAllUsers());
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Response> updateUser(@PathVariable UUID id, @RequestBody @Valid UserDTO dto) {
        return ResponseEntity.ok(service.updateUser(id, dto));
    }

    @PreAuthorize("hasAuthority('MANAGER')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteUser(@PathVariable UUID id) {
        return ResponseEntity.ok(service.deleteUser(id));
    }

    @GetMapping("/current")
    public ResponseEntity<User> getCurrentLoggedInUser() {
        return ResponseEntity.ok(service.getCurrentLoggedInUser());
    }
}
