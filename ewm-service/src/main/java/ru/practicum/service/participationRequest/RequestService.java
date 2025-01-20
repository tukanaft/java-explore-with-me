package ru.practicum.service.participationRequest;

import ru.practicum.dto.participationRequest.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    ParticipationRequestDto addRequest(Integer userId, Integer eventId);

    List<ParticipationRequestDto> getRequestsByUser(Integer userId);

    ParticipationRequestDto cancelRequest(Integer userId, Integer requestId);
}
