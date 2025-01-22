package ru.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Compilation.Compilation;

import java.util.List;

@org.springframework.stereotype.Repository
public interface CompilationRepository extends JpaRepository<Compilation, Integer> {

    List<Compilation> findAllByPinned(Boolean pinned, Pageable pageable);
}
