package ru.practicum.service.category;

import ru.practicum.dto.category.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto addCategory(String name);

    CategoryDto updateCategory(Integer catId, String name);

    void deleteCategory(Integer catId);

    List<CategoryDto> getCategories(Integer from, Integer size);

    CategoryDto getCategory(Integer catId);
}
