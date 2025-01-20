package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Compilation.Compilation;

import java.util.List;

@org.springframework.stereotype.Repository
public interface CompilationRepository extends JpaRepository<Compilation, Integer> {

    @Query(value = "SELECT c from compilations c LIMIT ?3 OFFSET ?2 " +
            "WHERE c.pinned = ?1", nativeQuery = true)
    List<Compilation> findCompilationsByPinned(Boolean pinned, Integer from, Integer size);
}
