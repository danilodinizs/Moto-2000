package dev.danilo.moto2000.service;

import dev.danilo.moto2000.dto.ProductDTO;
import dev.danilo.moto2000.dto.Response;
import dev.danilo.moto2000.dto.SupplierDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface ProductService {
    Response saveProduct(ProductDTO productDTO, MultipartFile imageFile);
    Response updateProduct(ProductDTO productDTO, MultipartFile imageFile);
    Response getAllProducts();
    Response getProductById(UUID id);
    void deleteProduct(UUID id);
}
