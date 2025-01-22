package ru.practicum.service.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.CompilationMapper;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.model.Compilation.Compilation;
import ru.practicum.model.event.Event;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;

import java.util.List;

@Slf4j
@org.springframework.stereotype.Service
@Component
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    final EventRepository eventRepository;
    final CompilationRepository compilationRepository;
    final CompilationMapper compilationMapper;

    @Override
    public CompilationDto addCompilation(NewCompilationDto compilationDto) {
        log.info("CompilationService: добавление подборки");
        List<Event> events = eventRepository.findAllByIdIn(compilationDto.getEventsId());
        Compilation compilation = compilationValidation(compilationMapper.newToCompilation(compilationDto, events));
        return compilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public CompilationDto updateCompilation(Integer compId, UpdateCompilationRequest compilationUpdate) {
        log.info("CompilationService: обновление подборки");
        Compilation compilation = findCompilation(compId);
        if (compilationUpdate.getEventsId() != null) {
            compilation.setEvents(eventRepository.findAllByIdIn(compilationUpdate.getEventsId()));
        }
        if (compilationUpdate.getPinned() != null) {
            compilation.setPinned(compilationUpdate.getPinned());
        }
        if (compilationUpdate.getTitle() != null) {
            compilation.setTitle(compilation.getTitle());
        }
        return compilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public void deleteCompilation(Integer compId) {
        log.info("CompilationService: удаление подборки");
        findCompilation(compId);
        compilationRepository.deleteById(compId);
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        log.info("CompilationService: отправление подборок");
        return compilationMapper.toCompilationDtoList(compilationRepository.findAllByPinned(pinned, PageRequest.of(from, size)));
    }

    @Override
    public CompilationDto getCompilation(Integer compId) {
        log.info("CompilationService: отправление подборки");
        return compilationMapper.toCompilationDto(findCompilation(compId));
    }

    private Compilation findCompilation(Integer compId) {
        return compilationRepository.findById(compId).orElseThrow(()
                -> new NotFoundException("подборки c нет в базе"));
    }

    private Compilation compilationValidation(Compilation compilation) {
        if (compilation.getTitle() == null || compilation.getTitle().length() > 50) {
            throw new ValidationException("не коректное название подборки");
        }
        if (compilation.getPinned() == null) {
            compilation.setPinned(false);
        }
        return compilation;
    }
}
