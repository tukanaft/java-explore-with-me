package ru.practicum.service.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.CategoryMapper;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.model.category.Category;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;

import java.util.List;

@Slf4j
@org.springframework.stereotype.Service
@Component
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    final EventRepository eventRepository;
    final CategoryRepository categoryRepository;
    final CategoryMapper categoryMapper;

    @Override
    public CategoryDto addCategory(String name) {
        log.info("CategoryService: добавление категории");
        Category category = new Category(null, name);
        return categoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto updateCategory(Integer catId, String name) {
        log.info("CategoryService: обновление категории: {}", catId);
        isCategoryExists(catId);
        Category category = new Category(catId, name);
        return categoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(Integer catId) {
        log.info("CategoryService: удаление категории: {}", catId);
        isCategoryExists(catId);
        if (eventRepository.findAllByCategory_Id(catId) != null){
            throw new ValidationException("есть события имеющие эту категорию");
        }
        categoryRepository.deleteById(catId);
    }

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        log.info("CategoryService: отправление данных о категориях");
        return categoryMapper.toCategoryDtoList(categoryRepository.findAllCat(from, size));
    }

    @Override
    public CategoryDto getCategory(Integer catId) {
        log.info("CategoryService: отправление данных о категории");
        return categoryMapper.toCategoryDto(isCategoryExists(catId));
    }

    private Category isCategoryExists(Integer catId){
        return categoryRepository.findById(catId).orElseThrow(()
                -> new NotFoundException("категории c нет в базе"));
    }
}
