package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.service.category.CategoryService;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/admin/categories")
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public CategoryDto addCategory(@RequestBody String name) {
        log.info("AdminCategoryController: выполнение запроса на добавление категории");
        return categoryService.addCategory(name);
    }

    @PatchMapping("/{catId}")
    public CategoryDto updateCategory(@PathVariable Integer catId, @RequestBody String name) {
        log.info("AdminCategoryController: выполнение запроса на обновление категории: {}", catId);
        return categoryService.updateCategory(catId, name);
    }

    @DeleteMapping("/{catId}")
    public void deleteCategory(@PathVariable Integer catId) {
        log.info("AdminCategoryController: выполнение запроса на удаление категории: {}", catId);
        categoryService.deleteCategory(catId);
    }
}
