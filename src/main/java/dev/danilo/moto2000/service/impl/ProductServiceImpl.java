package dev.danilo.moto2000.service.impl;

import dev.danilo.moto2000.dto.ProductDTO;
import dev.danilo.moto2000.dto.Response;
import dev.danilo.moto2000.repository.CategoryRepository;
import dev.danilo.moto2000.repository.ProductRepository;
import dev.danilo.moto2000.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    private final ModelMapper mapper;

    private final CategoryRepository categoryRepository;

    private static final String IMAGE_DRECTORY = System.getProperty("user.dir") + "/product-image";

    @Override
    public Response saveProduct(ProductDTO productDTO, MultipartFile imageFile) {
        return null;
    }

    @Override
    public Response updateProduct(ProductDTO productDTO, MultipartFile imageFile) {
        return null;
    }

    @Override
    public Response getAllProducts() {
        return null;
    }

    @Override
    public Response getProductById(UUID id) {
        return null;
    }

    @Override
    public void deleteProduct(UUID id) {

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

        String uniqueFileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
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
