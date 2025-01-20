package ru.practicum.dto.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.model.category.Category;
import ru.practicum.model.event.Event;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class CategoryMapper {

    public CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName()
        );
    }

    public Category toCategory(CategoryDto category) {
        return new Category(
                category.getId(),
                category.getName()
        );
    }

    public List<CategoryDto> toCategoryDtoList(List<Category> categories) {
        List<CategoryDto> categoriesDto = new ArrayList<>();
        for (Category category : categories) {
            categoriesDto.add(new CategoryDto(
                    category.getId(),
                    category.getName()
            ));
        }
        return categoriesDto;
    }
}
