package dev.danilo.moto2000.service;

import dev.danilo.moto2000.dto.CategoryDTO;
import dev.danilo.moto2000.dto.Response;

import java.util.UUID;

public interface CategoryService {

    Response createCategory(CategoryDTO dto);
    Response getAllCategories();
    Response getCategoryById(UUID id);
    Response updateCategory(UUID id, CategoryDTO dto);
    Response deleteCategory(UUID id);
}
