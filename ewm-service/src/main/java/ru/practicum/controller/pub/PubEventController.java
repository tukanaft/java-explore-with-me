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
    public List<EventFullDto> getEventsFiltered(@RequestParam(required = false) String text,
                                                @RequestParam(required = false) List<Integer> categories,
                                                @RequestParam(required = false) Boolean paid,
                                                @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                @RequestParam(defaultValue = "false") Boolean onlyAvailable, @RequestParam(required = false) String sort,
                                                @RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "10") Integer size,
                                                HttpServletRequest request) {
        log.info("PudEventController выполнение запроса на отправление информации о ивентах по фильтру");
        client.saveStats("ewm-server", request.getRequestURI(), request.getRemoteAddr());
        List<EventFullDto> events = eventService.getEventsFiltered(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        return events;
    }

    @GetMapping("/{id}")
    public EventFullDto getEventById(@PathVariable Integer id, HttpServletRequest request) {
        log.info("PudEventController выполнение запроса на отправление информации о ивенте");
        client.saveStats("ewm-server", request.getRequestURI(), request.getRemoteAddr());
        EventFullDto event = eventService.getEventById(id);
        return event;
    }
}
