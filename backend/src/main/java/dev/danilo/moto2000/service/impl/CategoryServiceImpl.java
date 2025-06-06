package dev.danilo.moto2000.service.impl;

import dev.danilo.moto2000.dto.CategoryDTO;
import dev.danilo.moto2000.dto.Response;
import dev.danilo.moto2000.entity.Category;
import dev.danilo.moto2000.exceptions.DataAlreadyExistsException;
import dev.danilo.moto2000.exceptions.NotFoundException;
import dev.danilo.moto2000.repository.CategoryRepository;
import dev.danilo.moto2000.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;
    private final ModelMapper mapper;

    @Override
    public Response createCategory(CategoryDTO dto) {
        Category category = mapper.map(dto, Category.class);

        repository.save(category);

        return Response.builder()
                .status(200)
                .message("Categoria criada com sucesso")
                .build();
    }

    @Override
    public Response getAllCategories() {
        List<Category> categories = repository.findAll();
        List<CategoryDTO> categoriesDTO = categories.stream().map(category -> mapper.map(category, CategoryDTO.class)).collect(Collectors.toList());

        return Response.builder()
                .status(200)
                .message("Sucesso")
                .categories(categoriesDTO)
                .build();
    }

    @Override
    public Response getCategoryById(UUID id) {
        Category category = repository.findById(id).orElseThrow(() -> new NotFoundException("Categoria não encontrada"));

        CategoryDTO dto = mapper.map(category, CategoryDTO.class);

        return Response.builder()
                .status(200)
                .message("Sucesso")
                .category(dto)
                .build();
    }

    @Override
    public Response updateCategory(UUID id, CategoryDTO categoryDTO) {
        Category category = repository.findById(id).orElseThrow(() -> new NotFoundException("Categoria não encontrada"));

        if (repository.existsByName(categoryDTO.getName())) {
            Response conflictResponse = Response.builder()
                    .status(409)
                    .message("Categoria " + categoryDTO.getName() + " já existe")
                    .category(categoryDTO)
                    .build();
            throw new DataAlreadyExistsException(conflictResponse);
        }

        categoryDTO.setId(category.getId());

        Category newCategory = mapper.map(categoryDTO, Category.class);

        repository.save(newCategory);

        return Response.builder()
                .status(200)
                .message("Sucesso")
                .category(categoryDTO)
                .build();
    }

    @Override
    public void deleteCategory(UUID id) {
        Category category = repository.findById(id).orElseThrow(() -> new NotFoundException("Categoria não encontrada"));

        repository.delete(category);

    }
}
