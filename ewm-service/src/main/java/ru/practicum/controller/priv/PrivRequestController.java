package ru.practicum.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventUserRequest;
import ru.practicum.dto.participationRequest.ParticipationRequestDto;
import ru.practicum.service.participationRequest.RequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/users/{userId}/requests")
public class PrivRequestController {
    private final RequestService requestService;

    @PostMapping
    public ParticipationRequestDto addRequest(@PathVariable Integer userId, @RequestParam Integer eventId) {
        log.info("PrivRequestController выполнение запроса на добавление запроса на участие");
        return requestService.addRequest(userId, eventId);
    }

    @GetMapping
    public List<ParticipationRequestDto> getRequestsByUser(@PathVariable Integer userId) {
        log.info("PrivRequestController выполнение запроса на отправление информации о запросах на участие созданных пользователем");
        return requestService.getRequestsByUser(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable Integer userId, @PathVariable Integer requestId) {
        log.info("PrivReqiestController выполнение запроса на отмену запроса на участие");
        return requestService.cancelRequest(userId, requestId);
    }

}
