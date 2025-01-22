package ru.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.event.Event;

import java.time.LocalDateTime;
import java.util.List;

@org.springframework.stereotype.Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    List<Event> findAllByInitiator_id(Integer initiatorId, Pageable pageable);

    List<Event> findAllByCategory_Id(Integer categoryId);

    List<Event> findAllByIdIn(List<Integer> ids);

    @Query(value = "SELECT e from events e " +
            "WHERE (UPPER(e.annotation) LIKE UPPER(CONCAT('%', ?1, '%'))) " +
            "OR UPPER(e.description) LIKE UPPER(CONCAT('%', ?1, '%'))) " +
            "AND e.paid = ?3 " +
            "AND e.event_date BETWEEN ?4 AND ?5 " +
            "AND e.category_id in ?2 " +
            "ODER BY e.views", nativeQuery = true)
    List<Event> findEventsAllFilteredByViews(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart,
                                             LocalDateTime rangeEnd, Pageable pageable);

    @Query(value = "SELECT e from events e " +
            "WHERE (UPPER(e.annotation) LIKE UPPER(CONCAT('%', ?1, '%'))) " +
            "OR (UPPER(e.description) LIKE UPPER(CONCAT('%', ?1, '%'))) " +
            "AND e.paid = ?3 " +
            "AND e.event_date BETWEEN ?4 AND ?5 " +
            "AND e.category_id in ?2 " +
            "ODER BY e.event_date", nativeQuery = true)
    List<Event> findEventsAllFilteredByDate(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart,
                                            LocalDateTime rangeEnd, Pageable pageable);

    @Query(value = "SELECT e from events e " +
            "WHERE (UPPER(e.annotation) LIKE UPPER(CONCAT('%', ?1, '%')) " +
            "OR UPPER(e.description) LIKE UPPER(CONCAT('%', ?1, '%')))) " +
            "AND e.paid = ?3 " +
            "AND e.event_date BETWEEN ?4 AND ?5 " +
            "AND e.category_id in ?2 ", nativeQuery = true)
    List<Event> findEventsAllNotFiltered(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart,
                                            LocalDateTime rangeEnd, Pageable pageable);

    @Query("SELECT e from Event e " +
            "WHERE e.eventDate BETWEEN ?4 AND ?5 " +
            "AND ( e.id in ?1 OR ?1 IS NULL) " +
            "AND ( e.state in ?2 OR ?2 IS NULL) " +
            "AND ( e.category.id in ?3 OR ?3 IS NULL) ")
    List<Event> findEventsALLAdminSearch(List<Integer> userIds, List<String> states, List<Integer> categories,
                                         LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);
}
