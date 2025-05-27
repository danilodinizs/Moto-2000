package dev.danilo.moto2000.controller;

import dev.danilo.moto2000.dto.LoginRequest;
import dev.danilo.moto2000.dto.RegisterRequest;
import dev.danilo.moto2000.dto.Response;
import dev.danilo.moto2000.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService service;

    @PostMapping("/register")
    public ResponseEntity<Response> registerUser(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.ok(service.registerUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(
            @RequestBody @Valid LoginRequest request,
            HttpServletResponse response
    ) {
        String token = service.loginAndGetToken(request);

        ResponseCookie cookie = ResponseCookie.from("jwt-token", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(7 * 24 * 3600)
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("Login successful")
                .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletResponse response) {
        ResponseCookie deleteCookie = ResponseCookie.from("jwt-token", "")
                .httpOnly(true)
                .secure(true) // true em prod
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());

        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("Logout successful")
                .build());
    }
}