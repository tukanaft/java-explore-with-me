package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.location.Location;

@org.springframework.stereotype.Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {
}
