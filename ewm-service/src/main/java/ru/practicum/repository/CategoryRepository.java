package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.category.Category;

@org.springframework.stereotype.Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
