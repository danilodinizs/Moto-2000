package dev.danilo.moto2000.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDTO {

        private UUID id;

        @NotBlank(message = "O nome é obrigatório")
        private String name;

        private String sku;

        @NotNull(message = "O preço é obrigatório")
        @Positive(message = "O valor do produto deve ser positivo")
        private BigDecimal price;

        @NotNull(message = "A quantidade em estoque é obrigatória")
        @Min(value = 0, message = "A quantidade em estoque não pode ser menor que zero")
        private Integer stockQuantity;

        @NotBlank(message = "A descrição é obrigatória")
        private String description;

        private LocalDateTime expiryDate;

        private String imageUrl;

        private UUID categoryId;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;

}
