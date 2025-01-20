package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.event.Event;

import java.time.LocalDateTime;
import java.util.List;

@org.springframework.stereotype.Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    @Query(value = "SELECT e from events e LEFT JOIN users u ON e.initiator_id = u.id LIMIT ?3 OFFSET ?2 " +
            "WHERE u.id = ?1", nativeQuery = true)
    List<Event> findAllByInitiator(Integer initiatorId, Integer from, Integer size);

    List<Event> findAllByCategory_Id(Integer categoryId);

    @Query(value = "SELECT e from events e " +
            "WHERE e.id in ?1", nativeQuery = true)
    List<Event> findEventsByIds(List<Integer> ids);

    @Query(value = "SELECT e from events e LIMIT ?6 OFFSET ?7" +
            "WHERE (UPPER(e.annotation) LIKE UPPER(CONCAT('%', ?1, '%')) " +
            "OR UPPER(e.description) LIKE UPPER(CONCAT('%', ?1, '%')))) " +
            "AND e.paid = ?3 " +
            "AND e.event_date is BETWEEN ?4 AND ?5 " +
            "AND e.category_id in ?2 " +
            "ODER BY e.views", nativeQuery = true)
    List<Event> findEventsAllFilteredByViews(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart,
                                             LocalDateTime rangeEnd, Integer from, Integer size);

    @Query(value = "SELECT e from events e LIMIT ?6 OFFSET ?7 " +
            "WHERE (UPPER(e.annotation) LIKE UPPER(CONCAT('%', ?1, '%')) " +
            "OR UPPER(e.description) LIKE UPPER(CONCAT('%', ?1, '%')))) " +
            "AND e.paid = ?3 " +
            "AND e.event_date is BETWEEN ?4 AND ?5 " +
            "AND e.category_id in ?2 " +
            "ODER BY e.event_date", nativeQuery = true)
    List<Event> findEventsAllFilteredByDate(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart,
                                            LocalDateTime rangeEnd, Integer from, Integer size);

    @Query(value = "SELECT e from events e LIMIT ?5 OFFSET ?6 " +
            "WHERE e.event_date is BETWEEN ?4 AND ?5 " +
            "AND e.id in ?1 " +
            "AND e.state in ?2 " +
            "AND e.category_id in ?3 ", nativeQuery = true)
    List<Event> findEventsALLAdminSearch(List<Integer> userIds, List<String> states, List<Integer> categories,
                                         LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);
}
