package dev.danilo.moto2000.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.danilo.moto2000.enums.TransactionPaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionRequest {

        private List<TransactionItemRequest> items;

        @NotBlank(message = "A descrição é obrigatória")
        private String description;

        private TransactionPaymentMethod transactionPaymentMethod;

        private Set<UUID> serviceOrderIds = new HashSet<>();;

        private UUID clientId;

}
