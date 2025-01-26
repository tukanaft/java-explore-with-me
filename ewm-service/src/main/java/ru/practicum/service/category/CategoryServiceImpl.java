package ru.practicum.service.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.CategoryMapper;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.model.category.Category;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;

import java.util.List;
import java.util.Objects;

@Slf4j
@org.springframework.stereotype.Service
@Component
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    final EventRepository eventRepository;
    final CategoryRepository categoryRepository;
    final CategoryMapper categoryMapper;

    @Override
    public CategoryDto addCategory(CategoryDto category) {
        log.info("CategoryService: добавление категории");
        nameValidation(category, null);
        return categoryMapper.toCategoryDto(categoryRepository.save(categoryMapper.toCategory(category)));
    }

    @Override
    public CategoryDto updateCategory(Integer catId, CategoryDto category) {
        log.info("CategoryService: обновление категории: {}", catId);
        Category categorySaved = isCategoryExists(catId);
        nameValidation(category, categorySaved.getId());
        return categoryMapper.toCategoryDto(categoryRepository.save(categoryMapper.toCategory(category)));
    }

    @Override
    public void deleteCategory(Integer catId) {
        log.info("CategoryService: удаление категории: {}", catId);
        isCategoryExists(catId);
        if (!eventRepository.findAllByCategory_Id(catId).isEmpty()) {
            throw new ConflictException("есть события имеющие эту категорию");
        }
        categoryRepository.deleteById(catId);
    }

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        log.info("CategoryService: отправление данных о категориях");
        return categoryMapper.toCategoryDtoList(categoryRepository.findAll(PageRequest.of(from, size)).toList());
    }

    @Override
    public CategoryDto getCategory(Integer catId) {
        log.info("CategoryService: отправление данных о категории");
        return categoryMapper.toCategoryDto(isCategoryExists(catId));
    }

    private Category isCategoryExists(Integer catId) {
        return categoryRepository.findById(catId).orElseThrow(()
                -> new NotFoundException("категории c нет в базе"));
    }

    private void nameValidation(CategoryDto category, Integer catId) {
        String name = category.getName();
        if (name == null || name.length() > 50 || name.isBlank()) {
            throw new ValidationException("не корректное имя категории");
        }
        for (Category categoryToCheck : categoryRepository.findAll()) {
            if (categoryToCheck.getName().equals(name) && !Objects.equals(categoryToCheck.getId(), catId)) {
                throw new ConflictException("категория с таким именем уже существует");
            }
        }
    }
}
