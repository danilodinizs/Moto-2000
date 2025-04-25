package dev.danilo.moto2000.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.danilo.moto2000.entity.Client;
import dev.danilo.moto2000.entity.Product;
import dev.danilo.moto2000.entity.Supplier;
import dev.danilo.moto2000.enums.TransactionPaymentMethod;
import dev.danilo.moto2000.enums.TransactionStatus;
import dev.danilo.moto2000.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigInteger;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record TransactionRequest(

        UUID productId,

        @NotNull(message = "A quantidade de produtos é obrigatória")
        @Positive(message = "O valor deve ser maior que zero")
        Integer totalProducts,

        @NotNull(message = "O preço total é obrigatório")
        @Positive(message = "O valor deve ser maior que zero")
        BigInteger totalPrice,

        @NotBlank(message = "A descrição é obrigatória")
        String description,

        UUID clientId,

        UUID supplierId) {
}
