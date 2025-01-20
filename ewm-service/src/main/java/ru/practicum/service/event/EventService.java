package ru.practicum.service.event;

import ru.practicum.dto.event.*;
import ru.practicum.dto.participationRequest.EventRequestStatusUpdateRequest;
import ru.practicum.dto.participationRequest.EventRequestStatusUpdateResult;
import ru.practicum.dto.participationRequest.ParticipationRequestDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventFullDto addEvent(Integer userId, NewEventDto newEventDto);

    List<EventShortDto> getEventsByInitiator(Integer userId, Integer from, Integer size);

    EventFullDto getEventFullByInitiator(Integer eventId);

    EventFullDto updateEvent(Integer eventId, UpdateEventUserRequest updateRequest);

    EventFullDto updateEventByAdmin(Integer eventId, UpdateEventAdminRequest updateRequest);

    List<EventFullDto> getEventsBySearch(List<Integer> userIds, List<String> states, List<Integer> categories,
                                         LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    List<ParticipationRequestDto> getRequestInEvent(Integer userId, Integer eventId);

    EventRequestStatusUpdateResult requestStatusUpdate(Integer userId, Integer eventId,
                                                       EventRequestStatusUpdateRequest statusUpdateRequest);

    List<EventFullDto> getEventsFiltered(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size);

    EventFullDto getEventById(Integer eventId);
}
