package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.participationRequest.ParticipationRequest;
import ru.practicum.model.participationRequest.RequestStatus;

import java.util.List;

@org.springframework.stereotype.Repository
public interface RequestRepository extends JpaRepository<ParticipationRequest, Integer> {
    List<ParticipationRequest> findAllByRequester_Id(Integer requesterId);

    List<ParticipationRequest> findAllByEvent_Id(Integer eventId);

    List<ParticipationRequest> findAllByEvent_IdAndStatus(Integer eventId, RequestStatus status);

    List<ParticipationRequest> findAllByIdIn(List<Integer> ids);
}
