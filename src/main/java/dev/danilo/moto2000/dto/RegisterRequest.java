package dev.danilo.moto2000.dto;

import dev.danilo.moto2000.enums.UserRole;

public record RegisterRequest(String username, String password, UserRole role) {
}
