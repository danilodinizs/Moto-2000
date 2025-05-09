package dev.danilo.moto2000.entity;

import dev.danilo.moto2000.enums.TransactionPaymentMethod;
import dev.danilo.moto2000.enums.TransactionStatus;
import dev.danilo.moto2000.enums.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

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

    @Column(name = "total_products", nullable = false)
    private Integer totalProducts;

    @Column(nullable = false)
    private BigInteger totalPrice;

    @Column(name = "transaction_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "transaction_payment_method", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionPaymentMethod transactionPaymentMethod;

    @Column(name = "transaction_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    @Column(nullable = false)
    private String description;

    private String note;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany
    @JoinTable(
            name = "transaction_products",
            joinColumns = @JoinColumn(name = "transaction_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> products = new HashSet<>();

    @OneToMany
    @JoinColumn(name = "transaction_id")
    private Set<ServiceOrder> serviceOrders;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

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
