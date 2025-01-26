package ru.practicum.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventUserRequest;
import ru.practicum.dto.participationRequest.EventRequestStatusUpdateRequest;
import ru.practicum.dto.participationRequest.EventRequestStatusUpdateResult;
import ru.practicum.dto.participationRequest.ParticipationRequestDto;
import ru.practicum.service.event.EventService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/users/{userId}/events")
public class PrivEventController {
    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable Integer userId, @RequestBody NewEventDto newEventDto) {
        log.info("PrivEventController выполнение запроса на добавление события");
        return eventService.addEvent(userId, newEventDto);
    }

    @GetMapping
    public List<EventShortDto> getEventsByInitiator(@PathVariable Integer userId, @RequestParam(defaultValue = "0") Integer from,
                                                    @RequestParam(defaultValue = "10") Integer size) {
        log.info("PrivEventController выполнение запроса на отправление информации о событиях созданных пользователем");
        return eventService.getEventsByInitiator(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventFullByInitiator(@PathVariable Integer eventId) {
        log.info("PrivEventController выполнение запроса на отправление полной информации о событии пользователя");
        return eventService.getEventFullByInitiator(eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable Integer eventId, @RequestBody UpdateEventUserRequest updateRequest) {
        log.info("PrivEventController выполнение запроса на обновление информации о событии");
        return eventService.updateEvent(eventId, updateRequest);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestInEvent(@PathVariable Integer userId, @PathVariable Integer eventId) {
        log.info("PrivEventController выполнение запроса на отправление информации о заявке на участие пользователя в событии");
        return eventService.getRequestInEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult requestStatusUpdate(@PathVariable Integer userId, @PathVariable Integer eventId,
                                                              @RequestBody EventRequestStatusUpdateRequest statusUpdateRequest) {
        log.info("PrivEventController выполнение запроса на изменение статуса заявок на участие в событии пользователя");
        return eventService.requestStatusUpdate(userId, eventId, statusUpdateRequest);
    }

}
