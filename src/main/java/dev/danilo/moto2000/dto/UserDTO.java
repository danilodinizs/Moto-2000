package dev.danilo.moto2000.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.danilo.moto2000.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserDTO(

        UUID id,

        @NotBlank(message = "O usuário é obrigatório")
        String username,

        @NotBlank(message = "A senha é obrigatória")
        @JsonIgnore
        String password,

        UserRole role,

        LocalDateTime createdAt) {
}
