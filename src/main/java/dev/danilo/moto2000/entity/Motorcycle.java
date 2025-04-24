package dev.danilo.moto2000.entity;

import dev.danilo.moto2000.enums.MotorcycleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "motorcycle_tb")
public class Motorcycle {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank(message = "A placa da moto é obrigatório")
    @Column(unique = true, nullable = false, name = "license_plate")
    private String licensePlate;

    @NotBlank(message = "O modelo da moto é obrigatório")
    @Column(nullable = false)
    private String model;

    @NotBlank(message = "O ano da moto é obrigatório")
    @Column(nullable = false)
    private Integer year;

    @Enumerated(EnumType.STRING)
    private Color color;

    @NotBlank(message = "O tipo da moto é obrigatório")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MotorcycleType motorcycleType;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    private final LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

}
