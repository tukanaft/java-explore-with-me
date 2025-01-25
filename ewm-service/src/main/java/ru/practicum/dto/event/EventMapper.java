package ru.practicum.dto.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.dto.category.CategoryMapper;
import ru.practicum.dto.location.LocationMapper;
import ru.practicum.dto.users.UserMapper;
import ru.practicum.model.category.Category;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.State;
import ru.practicum.model.users.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class EventMapper {

    final CategoryMapper categoryMapper;
    final UserMapper userMapper;
    final LocationMapper locationMapper;

    public Event toEvent(NewEventDto eventDto, Category category, User user) {
        return new Event(
                null,
                eventDto.getAnnotation(),
                category,
                0,
                LocalDateTime.now(),
                eventDto.getDescription(),
                eventDto.getEventDate(),
                user,
                eventDto.getLocation(),
                eventDto.getPaid(),
                eventDto.getParticipantLimit(),
                null,
                eventDto.getRequestModeration(),
                State.PENDING,
                eventDto.title,
                0
        );
    }

    public EventFullDto toEventFullDto(Event event) {
        return new EventFullDto(
                event.getId(),
                event.getAnnotation(),
                categoryMapper.toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getCreatedOn(),
                event.getDescription(),
                event.getEventDate(),
                userMapper.toUserShortDto(event.getInitiator()),
                locationMapper.toLocationDto(event.getLocation()),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn(),
                event.getRequestModeration(),
                event.getState(),
                event.getTitle(),
                event.getViews()
        );
    }

    public EventShortDto toEventShortDto(Event event) {
        return new EventShortDto(
                event.getId(),
                event.getAnnotation(),
                categoryMapper.toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getEventDate(),
                userMapper.toUserShortDto(event.getInitiator()),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getTitle(),
                event.getViews());
    }

    public List<EventShortDto> toEventShortDtoList(List<Event> events) {
        List<EventShortDto> eventsShortDto = new ArrayList<>();
        for (Event event : events) {
            eventsShortDto.add(toEventShortDto(event));
        }
        return eventsShortDto;
    }

    public List<EventFullDto> toEventFullList(List<Event> events) {
        List<EventFullDto> eventsDto = new ArrayList<>();
        for (Event event : events) {
            eventsDto.add(toEventFullDto(event));
        }
        return eventsDto;
    }
}
