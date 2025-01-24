package ru.practicum.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.practicum.dto.category.CategoryMapper;
import ru.practicum.dto.event.*;
import ru.practicum.dto.location.LocationMapper;
import ru.practicum.dto.participationRequest.*;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.model.category.Category;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.State;
import ru.practicum.model.participationRequest.ParticipationRequest;
import ru.practicum.model.participationRequest.RequestStatus;
import ru.practicum.model.users.User;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@Component
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    final EventRepository eventRepository;
    final UserRepository userRepository;
    final CategoryRepository categoryRepository;
    final RequestRepository requestRepository;
    final EventMapper eventMapper;
    final CategoryMapper categoryMapper;
    final LocationMapper locationMapper;
    final RequestMapper requestMapper;

    @Override
    public EventFullDto addEvent(Integer userId, NewEventDto newEventDto) {
        log.info("EventService: добавление нового события");
        Category category = null;
        if (newEventDto.getCategory() != null) {
            category = categoryRepository.findById(newEventDto.getCategory()).orElseThrow(()
                    -> new NotFoundException("категории c нет в базе"));
        }
        User user = validateUser(userId);
        Event event = eventMapper.toEvent(newEventDto, category, user);
        Event eventValidated = eventValidation(event);
        return eventMapper.toEventFullDto(eventRepository.save(eventValidated));
    }

    @Override
    public List<EventShortDto> getEventsByInitiator(Integer userId, Integer from, Integer size) {
        log.info("EventService: отправление информации о событиях созданных пользователем");
        return eventMapper.toEventShortDtoList(eventRepository.findAllByInitiator_id(userId, PageRequest.of(from, size)));
    }

    @Override
    public EventFullDto getEventFullByInitiator(Integer eventId) {
        log.info("EventService: отправление информации о событии созданным пользователем");
        return eventMapper.toEventFullDto(getEvent(eventId));
    }

    @Override
    public EventFullDto updateEvent(Integer eventId, UpdateEventUserRequest updateRequest) {
        log.info("EventService: изменение информации о событии созданным пользователем");
        Event event = getEvent(eventId);
        if (event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("нельзя менять опубликованные события");
        }
        if (updateRequest.getAnnotation() != null) {
            event.setAnnotation(updateRequest.getAnnotation());
        }
        if (updateRequest.getCategory() != null) {
            event.setCategory(categoryMapper.toCategory(updateRequest.getCategory()));
        }
        if (updateRequest.getDescription() != null) {
            event.setDescription(updateRequest.getDescription());
        }
        if (updateRequest.getEventDate() != null) {
            eventStartValidation(updateRequest.getEventDate());
            event.setEventDate(updateRequest.getEventDate());
        }
        if (updateRequest.getLocation() != null) {
            event.setLocation(locationMapper.toLocation(updateRequest.getLocation(), eventId));
        }
        if (updateRequest.getPaid() != null) {
            event.setPaid(updateRequest.getPaid());
        }
        if (updateRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateRequest.getParticipantLimit());
        }
        if (updateRequest.getStateAction() != null) {
            if (updateRequest.getStateAction().equals(StateActionUser.SEND_TO_REVIEW)) {
                event.setState(State.PENDING);
            }
            if (updateRequest.getStateAction().equals(StateActionUser.CANCEL_REVIEW)) {
                event.setState(State.CANCELED);
            }
        }
        if (updateRequest.getTitle() != null) {
            event.setTitle(updateRequest.getTitle());
        }
        Event eventValidated = eventValidation(event);
        return eventMapper.toEventFullDto(eventRepository.save(eventValidated));
    }

    @Override
    public EventFullDto updateEventByAdmin(Integer eventId, UpdateEventAdminRequest updateRequest) {
        log.info("EventService: изменение информации админом о событии созданным пользователем");
        Event event = getEvent(eventId);
        if (event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("нельзя менять опубликованные события");
        }
        if (updateRequest.getAnnotation() != null) {
            event.setAnnotation(updateRequest.getAnnotation());
        }
        if (updateRequest.getCategory() != null) {
            event.setCategory(categoryRepository.findById(updateRequest.getCategory())
                    .orElseThrow(() -> new NotFoundException("категории нет в базе")));
        }
        if (updateRequest.getDescription() != null) {
            event.setDescription(updateRequest.getDescription());
        }
        if (updateRequest.getEventDate() != null) {
            eventStartValidation(updateRequest.getEventDate());
            event.setEventDate(updateRequest.getEventDate());
        }
        if (updateRequest.getLocation() != null) {
            event.setLocation(locationMapper.toLocation(updateRequest.getLocation(), eventId));
        }
        if (updateRequest.getPaid() != null) {
            event.setPaid(updateRequest.getPaid());
        }
        if (updateRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateRequest.getParticipantLimit());
        }
        if (updateRequest.getStateAction() != null) {
            if (event.getState().equals(State.CANCELED)) {
                throw new ConflictException("собфтие отменено");
            }
            if (updateRequest.getStateAction().equals(StateActionAdmin.PUBLISH_EVENT)) {
                event.setState(State.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            } else if (updateRequest.getStateAction().equals(StateActionAdmin.REJECT_EVENT)) {
                event.setState(State.CANCELED);
            }
        }
        if (updateRequest.getTitle() != null) {
            event.setTitle(updateRequest.getTitle());
        }
        Event eventValidated = eventValidation(event);
        return eventMapper.toEventFullDto(eventRepository.save(eventValidated));
    }

    @Override
    public List<EventFullDto> getEventsBySearch(List<Integer> userIds, List<String> states, List<Integer> categories,
                                                LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        log.info("EventService: отправление информации о событиях по поиску администратора");
        if (rangeStart == null) {
            rangeStart = LocalDateTime.now();
            rangeEnd = LocalDateTime.now().plusYears(1000);
        }
        return eventMapper.toEventFullList(eventRepository.findEventsALLAdminSearch(userIds, states, categories,
                rangeStart, rangeEnd, PageRequest.of(from, size)));
    }

    @Override
    public List<ParticipationRequestDto> getRequestInEvent(Integer userId, Integer eventId) {
        log.info("EventService: отправление информации о заявках на участие в событии созданным пользователем");
        validateUser(userId);
        getEvent(eventId);
        return requestMapper.toRequestDtoList(requestRepository.findAllByEvent_Id(eventId));
    }

    @Override
    public EventRequestStatusUpdateResult requestStatusUpdate(Integer userId, Integer eventId, EventRequestStatusUpdateRequest statusUpdateRequest) {
        log.info("EventService: изменение статуса заявок на участие в событии созданным пользователем");
        validateUser(userId);
        Event event = getEvent(eventId);
        List<ParticipationRequest> approvedRequests = new ArrayList<>();
        List<ParticipationRequest> rejectedRequests = new ArrayList<>();
        List<ParticipationRequest> requests = requestRepository.findAllByIdIn(statusUpdateRequest.getRequestIds());
        if (statusUpdateRequest.getStatus().equals(UpdateStatus.CONFIRMED)) {
            if (Objects.equals(event.getConfirmedRequests(), event.getParticipantLimit()) && event.getParticipantLimit() != 0) {
                throw new ConflictException("достигнут лимит подтвержденных заявок на участие");
            }
            for (ParticipationRequest request : requests) {
                validateRequestStatus(request);
                request.setStatus(RequestStatus.CONFIRMED);
                approvedRequests.add(request);
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                if (Objects.equals(event.getConfirmedRequests(), event.getParticipantLimit()) &&
                        requests.size() != approvedRequests.size()) {
                    rejectedRequests = requestRepository.findAllByEvent_IdAndStatus(eventId, RequestStatus.PENDING);
                    for (ParticipationRequest rejRequest : rejectedRequests) {
                        rejRequest.setStatus(RequestStatus.REJECTED);
                    }
                    break;
                }
            }
            List<ParticipationRequest> allRequests = new ArrayList<>(approvedRequests);
            allRequests.addAll(rejectedRequests);
            requestRepository.saveAll(allRequests);
            return new EventRequestStatusUpdateResult(requestMapper.toRequestDtoList(approvedRequests),
                    requestMapper.toRequestDtoList(rejectedRequests));
        } else if (statusUpdateRequest.getStatus().equals(UpdateStatus.REJECTED)) {
            for (ParticipationRequest request : requests) {
                validateRequestStatus(request);
                request.setStatus(RequestStatus.REJECTED);
                rejectedRequests.add(request);
            }
            requestRepository.saveAll(rejectedRequests);
            return new EventRequestStatusUpdateResult(null,
                    requestMapper.toRequestDtoList(rejectedRequests));
        }
        return null;
    }

    @Override
    public List<EventFullDto> getEventsFiltered(String text, List<Integer> categories, Boolean paid,
                                                LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                                String sort, Integer from, Integer size) {
        log.info("EventService: отправление информации о событии по фильтрации");
        if (text.equals("0")){
            text = null;
        }
        if (categories.getFirst().equals(0)){
            categories = null;
        }
        if (rangeStart == null) {
            rangeStart = LocalDateTime.now();
            rangeEnd = LocalDateTime.now().plusYears(1000);
        }
        switch (sort) {
            case ("EVENT_DATE"):
                List<Event> eventsByDate = eventRepository.findEventsAllFilteredByDate(text, categories, paid,
                        rangeStart, rangeEnd, PageRequest.of(from, size));
                eventStateValidation(eventsByDate);
                if (onlyAvailable) {
                    List<Event> eventsAvailable = new ArrayList<>();
                    for (Event event : eventsByDate) {
                        if (event.getConfirmedRequests() < event.getParticipantLimit()) {
                            eventsAvailable.add(event);
                        }
                    }
                    return eventMapper.toEventFullList(eventsAvailable);
                } else {
                    return eventMapper.toEventFullList(eventsByDate);
                }
            case ("VIEWS"):
                List<Event> eventsByViews = eventRepository.findEventsAllFilteredByViews(text, categories, paid,
                        rangeStart, rangeEnd, PageRequest.of(from, size));
                eventStateValidation(eventsByViews);
                if (onlyAvailable) {
                    List<Event> eventsAvailable = new ArrayList<>();
                    for (Event event : eventsByViews) {
                        if (event.getConfirmedRequests() < event.getParticipantLimit()) {
                            eventsAvailable.add(event);
                        }
                    }
                    return eventMapper.toEventFullList(eventsAvailable);
                } else {
                    return eventMapper.toEventFullList(eventsByViews);
                }
            case null:
                List<Event> events = eventRepository.findEventsAllNotFiltered(text, categories, paid, rangeStart,
                        rangeEnd, PageRequest.of(from, size));
                eventStateValidation(events);
                if (onlyAvailable) {
                    List<Event> eventsAvailable = new ArrayList<>();
                    for (Event event : events) {
                        if (event.getConfirmedRequests() < event.getParticipantLimit()) {
                            eventsAvailable.add(event);
                        }
                    }
                    return eventMapper.toEventFullList(eventsAvailable);
                } else {
                    return eventMapper.toEventFullList(events);
                }
            default:
                throw new IllegalStateException("Unexpected value: " + sort);
        }
    }

    @Override
    public EventFullDto getEventById(Integer eventId) {
        log.info("EventService: отправление информации о событии по id");
        Event event = getEvent(eventId);
        if (event.getState() != State.PUBLISHED) {
            throw new NotFoundException("ивент еще не был опубликован");
        }
        event.setViews(event.getViews() + 1);
        return eventMapper.toEventFullDto(event);
    }

    private User validateUser(Integer userId) {
        return userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("категории c нет в базе"));
    }

    private Event getEvent(Integer eventId) {
        return eventRepository.findById(eventId).orElseThrow(()
                -> new NotFoundException("категории c нет в базе"));
    }

    private Event eventValidation(Event event) {
        String annotation = event.getAnnotation();
        String description = event.getDescription();
        String title = event.getTitle();
        if (annotation == null || annotation.length() < 20 || annotation.length() > 2000 || annotation.isBlank()) {
            throw new ValidationException("не корректная аннотация");
        }
        if (description == null || description.length() < 20 || description.length() > 7000 || description.isBlank()) {
            throw new ValidationException("не корректное описание");
        }
        if (event.getPaid() == null) {
            event.setPaid(false);
        }
        if (event.getParticipantLimit() == null) {
            event.setParticipantLimit(0);
        }
        if (event.getRequestModeration() == null) {
            event.setRequestModeration(true);
        }
        if (title == null || title.length() < 3 || title.length() > 120 || title.isBlank()) {
            throw new ValidationException("не корректный заголовок");
        }
        eventStartValidation(event.getEventDate());
        if (event.getParticipantLimit() < 0) {
            throw new ValidationException("отрицательный лимит участников");
        }
        return event;
    }

    private void eventStartValidation(LocalDateTime eventStart) {
        if (eventStart == null || LocalDateTime.now().plusHours(2).isAfter(eventStart)) {
            throw new ValidationException("не верно введно время начала события");
        }
    }

    private void validateRequestStatus(ParticipationRequest request) {
        if (!(request.getStatus().equals(RequestStatus.PENDING))) {
            throw new ConflictException("статус заяки не в ожидании");
        }
    }

    private void eventStateValidation(List<Event> events) {
        for (Event event : events) {
            if (event.getState() != State.PUBLISHED) {
                throw new ValidationException("собфтие не опубликованно");
            }
        }
    }
}
