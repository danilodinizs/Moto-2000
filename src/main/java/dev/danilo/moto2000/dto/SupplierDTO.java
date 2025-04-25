package dev.danilo.moto2000.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record SupplierDTO(

        UUID id,

        @NotBlank(message = "O nome é obrigatório")
        String name,

        @NotBlank(message = "O CNPJ é obrigatório")
        String cnpj,

        @NotBlank(message = "O contato é obrigatório")
        String contactInfo,

        String address) {
}
