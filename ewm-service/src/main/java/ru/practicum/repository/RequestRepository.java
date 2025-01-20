package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.participationRequest.ParticipationRequest;
import ru.practicum.model.participationRequest.RequestStatus;

import java.util.List;

@org.springframework.stereotype.Repository
public interface RequestRepository extends JpaRepository<ParticipationRequest, Integer> {
    List<ParticipationRequest> findAllByRequester_Id(Integer requesterId);

    List<ParticipationRequest> findAllByEventId(Integer eventId);

    List<ParticipationRequest> findAllByEventIdAndStatus(Integer eventId, RequestStatus status);

    @Query(value = "SELECT r from requests r  " +
            "WHERE r.id in ?1", nativeQuery = true)
    List<ParticipationRequest> findRequestsByIds(List<Integer> ids);
}
