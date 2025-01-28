package ru.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.comment.Comment;

import java.util.List;

@org.springframework.stereotype.Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findAllByAuthor_IdAndIsPositive(Integer authorId, Boolean isPositive, Pageable pageable);

    List<Comment> findAllByAuthor_Id(Integer authorId, Pageable pageable);

    List<Comment> findAllByEvent_Id(Integer eventId);

    List<Comment> findAllByEvent_IdAndIsPositive(Integer eventId, Boolean isPositive);

    @Query("SELECT c from Comment c " +
            "WHERE (UPPER(c.text) LIKE UPPER(CONCAT('%', ?1, '%')))")
    List<Comment> findCommentsByText(String text);
}
