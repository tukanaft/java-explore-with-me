package ru.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.users.User;

import java.util.List;

@org.springframework.stereotype.Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findAllByIdIn(List<Integer> ids, Pageable pageable);
}
