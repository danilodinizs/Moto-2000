package dev.danilo.moto2000.dto;

import dev.danilo.moto2000.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "O usuário é obrigatório")
    private String username;

    @NotBlank(message = "A senha é obrigatória")
    private String password;

    private UserRole role;
}
