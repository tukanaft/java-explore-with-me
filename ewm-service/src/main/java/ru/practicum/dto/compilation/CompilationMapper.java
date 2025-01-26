package ru.practicum.dto.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.dto.event.EventMapper;
import ru.practicum.model.Compilation.Compilation;
import ru.practicum.model.event.Event;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class CompilationMapper {

    final EventMapper eventMapper;

    public Compilation newToCompilation(NewCompilationDto compilationDto, List<Event> events) {
        return new Compilation(
                null,
                events,
                compilationDto.getPinned(),
                compilationDto.getTitle()
        );
    }

    public CompilationDto toCompilationDto(Compilation compilation) {
        return new CompilationDto(
                compilation.getId(),
                eventMapper.toEventShortDtoList(compilation.getEvents()),
                compilation.getPinned(),
                compilation.getTitle()
        );
    }

    public List<CompilationDto> toCompilationDtoList(List<Compilation> compilations) {
        List<CompilationDto> compilationsDto = new ArrayList<>();
        for (Compilation compilation : compilations) {
            compilationsDto.add(toCompilationDto(compilation));
        }
        return compilationsDto;
    }
}
