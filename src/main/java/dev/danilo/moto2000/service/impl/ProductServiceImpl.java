package dev.danilo.moto2000.service.impl;

import dev.danilo.moto2000.dto.ProductDTO;
import dev.danilo.moto2000.dto.Response;
import dev.danilo.moto2000.entity.Category;
import dev.danilo.moto2000.entity.Product;
import dev.danilo.moto2000.entity.Supplier;
import dev.danilo.moto2000.exceptions.NotFoundException;
import dev.danilo.moto2000.repository.CategoryRepository;
import dev.danilo.moto2000.repository.ProductRepository;
import dev.danilo.moto2000.repository.SupplierRepository;
import dev.danilo.moto2000.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    private final ModelMapper mapper;

    private final CategoryRepository categoryRepository;

    private final SupplierRepository supplierRepository;

    private static final String IMAGE_DRECTORY = System.getProperty("user.dir") + "/src/main/resources/static/images/";

    @Override
    public Response saveProduct(ProductDTO productDTO, MultipartFile imageFile) {
        Category category = categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(() -> new NotFoundException("Categoria não encontrada"));

        Supplier supplier = supplierRepository.findById(productDTO.getSupplierId()).orElseThrow(() -> new NotFoundException("Fonercedor não encontrado"));

        Product product = mapper.map(productDTO, Product.class);

        product.setCategory(category);
        product.setSupplier(supplier);

        if(imageFile != null) {
            String imagePath = saveImage(imageFile);
            product.setImageUrl(imagePath);
        }

        repository.save(product);

        return Response.builder()
                .status(200)
                .message("Sucesso")
                .build();
    }

    @Override
    public Response updateProduct(ProductDTO productDTO, MultipartFile imageFile) {
        repository.findById(productDTO.getProductId()).orElseThrow(() -> new NotFoundException("Produto não encontrado"));

        Product product = mapper.map(productDTO, Product.class);

        if (imageFile != null && !imageFile.isEmpty()) {
            String imagePath = saveImage(imageFile);
            product.setImageUrl(imagePath);
        }

        if (productDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(() -> new NotFoundException("Categoria não encontrada"));

            product.setCategory(category);
        }

        if (productDTO.getCategoryId() != null) {
            Supplier supplier = supplierRepository.findById(productDTO.getSupplierId()).orElseThrow(() -> new NotFoundException("Fonercedor não encontrado"));

            product.setSupplier(supplier);
        }

        product.setUpdatedAt(LocalDateTime.now());

        repository.save(product);

        return Response.builder()
                .status(200)
                .message("Produto atualizado com sucesso")
                .build();
    }

    @Override
    public Response getAllProducts() {
        List<Product> products = repository.findAll();

        List<ProductDTO> productsDTO = products.stream().map(product -> mapper.map(product, ProductDTO.class)).collect(Collectors.toList());

        return Response.builder()
                .status(200)
                .message("Sucesso")
                .products(productsDTO)
                .build();
    }

    @Override
    public Response getProductById(UUID id) {
        Product product = repository.findById(id).orElseThrow(() -> new NotFoundException("Produto não encontrado"));

        ProductDTO productDTO = mapper.map(product, ProductDTO.class);

        return Response.builder()
                .status(200)
                .message("Sucesso")
                .product(productDTO)
                .build();
    }

    @Override
    public void deleteProduct(UUID id) {
        Product product = repository.findById(id).orElseThrow(() -> new NotFoundException("Produto não encontrado"));
        repository.delete(product);
    }

    private String saveImage(MultipartFile imageFile) {
        if(!imageFile.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Apenas imagens são aceitas");
        }

        File directory = new File(IMAGE_DRECTORY);
        if(!directory.exists()) {
            directory.mkdir();
            log.info("Directory was created");
        }

        String uniqueFileName = "product-" + UUID.randomUUID() + "-" + imageFile.getOriginalFilename();
        String imagePath = IMAGE_DRECTORY + uniqueFileName;

        try {

            File destinationFile = new File(imagePath);
            imageFile.transferTo(destinationFile);

        } catch (Exception e) {
            throw new IllegalArgumentException("Ocorreu um erro ao salvar a imagem" + e.getMessage());
        }
        return imagePath;
    }
}
