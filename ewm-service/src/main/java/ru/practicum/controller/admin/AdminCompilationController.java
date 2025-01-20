package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;
import ru.practicum.service.compilation.CompilationService;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/admin/compilations")
public class AdminCompilationController {
    private final CompilationService compilationService;

    @PostMapping
    public CompilationDto addCompilation(@RequestBody NewCompilationDto compilationDto) {
        log.info("AdminCompilationController: выполнение запроса на добавление подборки");
        return compilationService.addCompilation(compilationDto);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(@PathVariable Integer compId,
                                            @RequestBody UpdateCompilationRequest compilationUpdate) {
        log.info("AdminCompilationController: выполнение запроса на обновление подборки: {}", compId);
        return compilationService.updateCompilation(compId, compilationUpdate);
    }

    @DeleteMapping("/{compId}")
    public void deleteCompilation(@PathVariable Integer compId) {
        log.info("AdminCompilationController: выполнение запроса на удаление подборки: {}", compId);
        compilationService.deleteCompilation(compId);
    }
}
