package ru.practicum.dto.participationRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.model.participationRequest.ParticipationRequest;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class RequestMapper {
    public ParticipationRequestDto toRequestDto(ParticipationRequest request) {
        return new ParticipationRequestDto(
                request.getId(),
                request.getCreated(),
                request.getEvent().getId(),
                request.getRequester().getId(),
                request.getStatus()
        );
    }

    public List<ParticipationRequestDto> toRequestDtoList(List<ParticipationRequest> requests){
        List<ParticipationRequestDto> requestsDto = new ArrayList<>();
        for (ParticipationRequest request : requests){
            requestsDto.add(toRequestDto(request));
        }
        return requestsDto;
    }
}
