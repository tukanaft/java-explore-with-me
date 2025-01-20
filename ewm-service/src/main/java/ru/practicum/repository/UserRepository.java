package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.users.User;

import java.util.List;

@org.springframework.stereotype.Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "SELECT * from users u LIMIT ?3 OFFSET ?2 " +
            "WHERE u.id in ?1", nativeQuery = true)
    List<User> findUsers(List<Integer> ids, Integer from, Integer size);
}
