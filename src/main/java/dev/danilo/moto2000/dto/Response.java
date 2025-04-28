package dev.danilo.moto2000.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.danilo.moto2000.enums.UserRole;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record Response(

        Integer status,
        String message,
        String token,
        UserRole role,
        String expirationTime,

        Integer totalPages,
        Long totalElements,

        UserDTO user,
        List<UserDTO> users,

        SupplierDTO supplier,
        List<SupplierDTO> suppliers,

        CategoryDTO category,
        List<CategoryDTO> categories,

        ProductDTO product,
        List<ProductDTO> products,

        TransactionDTO transaction,
        List<TransactionDTO> transactions,

        LocalDateTime timeStamp

    ) {
}
