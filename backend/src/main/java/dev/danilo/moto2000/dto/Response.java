package dev.danilo.moto2000.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.danilo.moto2000.enums.UserRole;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response {

        private Integer status;
        private String message;
        private String token;
        private UserRole role;
        private String expirationTime;

        private Integer totalPages;
        private Long totalElements;

        private UserDTO user;
        private List<UserDTO> users;

        private ClientDTO client;
        private List<ClientDTO> clients;

        private MotorcycleDTO motorcycle;
        private List<MotorcycleDTO> motorcycles;

        private SupplierDTO supplier;
        private List<SupplierDTO> suppliers;

        private CategoryDTO category;
        private List<CategoryDTO> categories;

        private ProductDTO product;
        private List<ProductDTO> products;

        private ServiceOrderDTO serviceOrder;
        private List<ServiceOrderDTO> serviceOrders;

        private TransactionDTO transaction;
        private List<TransactionDTO> transactions;

        private final LocalDateTime timeStamp = LocalDateTime.now();
}
