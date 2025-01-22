package ru.practicum.service.participationRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.dto.participationRequest.ParticipationRequestDto;
import ru.practicum.dto.participationRequest.RequestMapper;
import ru.practicum.exception.ConflictExeption;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.State;
import ru.practicum.model.participationRequest.ParticipationRequest;
import ru.practicum.model.participationRequest.RequestStatus;
import ru.practicum.model.users.User;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@org.springframework.stereotype.Service
@Component
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    final RequestRepository requestRepository;
    final EventRepository eventRepository;
    final UserRepository userRepository;
    final RequestMapper requestMapper;

    @Override
    public ParticipationRequestDto addRequest(Integer userId, Integer eventId) {
        log.info("RequestService: добавление нового запроса на участие");
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("события нет в базе"));
        User user = findUser(userId);
        if (Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ConflictExeption("пользователь явлеятся организатором ивента");
        }
        if ((event.getConfirmedRequests() + 1) > event.getParticipantLimit() && event.getParticipantLimit() != 0) {
            throw new ConflictExeption("колличество участников достигло максимума");
        }
        if (event.getState() != State.PUBLISHED) {
            throw new ConflictExeption("событие еще не опубликовано");
        }
        ParticipationRequest request = new ParticipationRequest(
                null,
                LocalDateTime.now(),
                event,
                user,
                RequestStatus.PENDING
        );
        if (!event.getRequestModeration()) {
            request.setStatus(RequestStatus.APPROVED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        }
        if (event.getParticipantLimit() == 0) {
            request.setStatus(RequestStatus.APPROVED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        }
        return requestMapper.toRequestDto(requestRepository.save(request));
    }

    @Override
    public List<ParticipationRequestDto> getRequestsByUser(Integer userId) {
        log.info("RequestService: добавление запросов на участие");
        findUser(userId);
        return requestMapper.toRequestDtoList(requestRepository.findAllByRequester_Id(userId));
    }

    @Override
    public ParticipationRequestDto cancelRequest(Integer userId, Integer requestId) {
        ParticipationRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("запроса нет в базе"));
        request.setStatus(RequestStatus.CANCELED);
        return requestMapper.toRequestDto(requestRepository.save(request));
    }

    private User findUser(Integer userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("пользователя нет в базе"));
    }
}
