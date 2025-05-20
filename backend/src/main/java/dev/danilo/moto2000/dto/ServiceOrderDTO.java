package dev.danilo.moto2000.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceOrderDTO {

    private UUID id;

    @NotBlank(message = "O nome é obrigatório")
    private String name;

    private String description;

    private String duration;

    @Positive(message = "O valor da mão de obra deve ser positivo")
    private BigDecimal laborCost;

    private LocalDateTime createdAt;

    private Set<UUID> productsIds = new HashSet<>();
}
