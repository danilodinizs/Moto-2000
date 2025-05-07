package dev.danilo.moto2000.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientDTO {

        private UUID id;

        @NotBlank(message = "O nome é obrigatório")
        private String name;

        @NotBlank(message = "O CPF é obrigatório")
        private String cpf;

        private String email;

        private String phoneNumber;

        private LocalDateTime createdAt;

        private List<MotorcycleDTO> motorcycles;

        private List<TransactionDTO> transactions;

}
