package ru.practicum.controller.pub;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.Client;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.service.event.EventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/events")
public class PubEventController {
    private final EventService eventService;
    private final Client client;

    @GetMapping
    public List<EventFullDto> getEventsFiltered(@RequestParam String text, @RequestParam List<Integer> categories,
                                                @RequestParam Boolean paid,
                                                @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                @RequestParam Boolean onlyAvailable, @RequestParam String sort,
                                                @RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "10") Integer size,
                                                HttpServletRequest request) {
        log.info("PudEventController выполнение запроса на отправление информации о ивентах по фильтру");
        List<EventFullDto> events = eventService.getEventsFiltered(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        client.saveStats("ewm-server", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());
        return events;
    }

    @GetMapping("/{id}")
    public EventFullDto getEventById(@PathVariable Integer id, HttpServletRequest request) {
        log.info("PudEventController выполнение запроса на отправление информации о ивенте");
        EventFullDto event = eventService.getEventById(id);
        client.saveStats("ewm-server", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());
        return event;
    }
}
