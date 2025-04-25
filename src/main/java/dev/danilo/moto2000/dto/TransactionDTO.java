package dev.danilo.moto2000.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.danilo.moto2000.enums.TransactionPaymentMethod;
import dev.danilo.moto2000.enums.TransactionStatus;
import dev.danilo.moto2000.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record TransactionDTO(

        UUID id,

        Integer totalProducts,

        BigInteger totalPrice,

        TransactionType transactionType,

        TransactionPaymentMethod transactionPaymentMethod,

        TransactionStatus transactionStatus,

        String description,

        LocalDateTime updatedAt,

        LocalDateTime createdAt,

        ClientDTO client,

        ProductDTO product,

        SupplierDTO supplier) {
}
