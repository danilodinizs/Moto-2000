package dev.danilo.moto2000.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "supplier_tb")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank(message = "O nome é obrigatório")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "O CNPJ é obrigatório")
    @Column(unique = true, nullable = false)
    private String cnpj;

    @NotBlank(message = "O contato é obrigatório")
    @Column(name = "contact_info", nullable = false)
    private String contactInfo;

    private String address;
}
