package dev.danilo.moto2000.dto;

import dev.danilo.moto2000.entity.Client;
import dev.danilo.moto2000.enums.MotorcycleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.UUID;

public record MotorcycleDTO(

        UUID id,

        @NotBlank(message = "A placa da moto é obrigatório")

        String licensePlate,

        @NotBlank(message = "O modelo da moto é obrigatório")
        String model,

        @NotNull(message = "O ano da moto é obrigatório")
        Integer year,

        Color color,

        @NotNull(message = "O tipo da moto é obrigatório")
        MotorcycleType motorcycleType,

        ClientDTO client,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
        ) {
}
