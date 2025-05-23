package dev.danilo.moto2000.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SupplierDTO {

        private UUID id;

        @NotBlank(message = "O nome é obrigatório")
        private String name;

        @NotBlank(message = "O CNPJ é obrigatório")
        private String cnpj;

        @NotBlank(message = "O contato é obrigatório")
        private String contactInfo;

        private String address;
}
