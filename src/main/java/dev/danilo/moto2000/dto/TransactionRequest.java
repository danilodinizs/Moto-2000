package dev.danilo.moto2000.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionRequest {

        private UUID productId;

        @NotNull(message = "A quantidade de produtos é obrigatória")
        private Integer totalProducts;

        @NotBlank(message = "A descrição é obrigatória")
        private String description;

        private List<UUID> serviceOrderIds = new ArrayList<>();;

        private UUID clientId;

        private UUID supplierId;
}
