package dev.danilo.moto2000.entity;

import dev.danilo.moto2000.enums.TransactionStatus;
import dev.danilo.moto2000.enums.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "transaction_tb")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull(message = "A quantidade de produtos é obrigatória")
    @Column(name = "total_products", nullable = false)
    private Integer totalProducts;

    @NotNull(message = "O preço total é obrigatório")
    @Column(nullable = false)
    private BigInteger totalPrice;

    @Column(name = "transaction_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "transaction_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    @NotBlank(message = "A descrição é obrigatória")
    @Column(nullable = false)
    private String description;

    private String note;

    private final LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @Override
    public String toString() {
        return "Transaction{" +
                "updatedAt=" + updatedAt +
                ", createdAt=" + createdAt +
                ", note='" + note + '\'' +
                ", transactionStatus=" + transactionStatus +
                ", description='" + description + '\'' +
                ", totalPrice=" + totalPrice +
                ", transactionType=" + transactionType +
                ", totalProducts=" + totalProducts +
                ", id=" + id +
                '}';
    }
}
