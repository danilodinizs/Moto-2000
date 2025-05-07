package dev.danilo.moto2000.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.danilo.moto2000.enums.TransactionPaymentMethod;
import dev.danilo.moto2000.enums.TransactionStatus;
import dev.danilo.moto2000.enums.TransactionType;
import lombok.*;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionDTO {

    private UUID id;

    private Integer totalProducts;

    private BigInteger totalPrice;

    private TransactionType transactionType;

    private TransactionPaymentMethod transactionPaymentMethod;

    private TransactionStatus transactionStatus;

    private String description;

    private LocalDateTime updatedAt;

    private LocalDateTime createdAt;

    private ClientDTO client;

    private ProductDTO product;

    private ServiceOrderDTO serviceOrder;

    private SupplierDTO supplier;
}
