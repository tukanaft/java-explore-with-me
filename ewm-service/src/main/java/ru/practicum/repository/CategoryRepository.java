package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.category.Category;

import java.util.List;

@org.springframework.stereotype.Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query(value = "SELECT u from categories u LIMIT ?2 OFFSET ?1", nativeQuery = true)
    List<Category> findAllCat(Integer from, Integer size);
}
