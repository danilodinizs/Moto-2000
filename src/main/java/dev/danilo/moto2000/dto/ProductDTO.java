package dev.danilo.moto2000.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.danilo.moto2000.entity.Category;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record ProductDTO(

        UUID id,

        @NotBlank(message = "O nome é obrigatório")
        String name,

        String sku,

        @NotNull(message = "O preço é obrigatório")
        @Positive(message = "O valor do produto deve ser positivo")
        BigDecimal price,

        @NotNull(message = "A quantidade em estoque é obrigatória")
        @Min(value = 0, message = "A quantidade em estoque não pode ser menor que zero")
        Integer stockQuantity,

        @NotBlank(message = "A descrição é obrigatória")
        String description,

        LocalDateTime expiryDate,

        String imageUrl,

        UUID categoryId,

        LocalDateTime createdAt,

        LocalDateTime updatedAt

        ) {
}
