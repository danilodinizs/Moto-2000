package dev.danilo.moto2000.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.danilo.moto2000.enums.MotorcycleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MotorcycleDTO {

        private UUID id;

        @NotBlank(message = "A placa da moto é obrigatório")
        private String licensePlate;

        @NotBlank(message = "O modelo da moto é obrigatório")
        private String model;

        @NotNull(message = "O ano da moto é obrigatório")
        private Integer year;

        private Color color;

        @NotNull(message = "O tipo da moto é obrigatório")
        private MotorcycleType motorcycleType;

        private UUID clientId;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;

}
