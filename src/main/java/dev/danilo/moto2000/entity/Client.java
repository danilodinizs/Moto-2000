package dev.danilo.moto2000.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "client_tb")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank(message = "O nome é obrigatório")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "O CPF é obrigatório")
    @Column(unique = true, nullable = false)
    private String cpf;

    @Column(unique = true)
    private String email;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @OneToMany(mappedBy = "client")
    private List<Motorcycle> motorcycles;

    @Column(name = "created_at")
    private final LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "client")
    private List<Transaction> transactions;

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cpf='" + cpf + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", motorcycles=" + motorcycles +
                ", createdAt=" + createdAt +
                '}';
    }
}
