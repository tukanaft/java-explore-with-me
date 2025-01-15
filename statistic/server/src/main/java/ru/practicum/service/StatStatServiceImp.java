package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.mapper.HitMapper;
import ru.practicum.repository.StatRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Slf4j
@org.springframework.stereotype.Service
@Component
@RequiredArgsConstructor
public class StatStatServiceImp implements StatService {

    final StatRepository repository;
    final HitMapper hitMapper;

    @Override
    public List<ViewStatsDto> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        log.info("StatService: выполнение запроса на отправление статистики");
        List<ViewStatsDto> stats;
        if (uris != null && !uris.isEmpty() && unique) {
            stats = repository.findStatsWithUriUnique(start, end, uris);
        } else if (uris != null && !uris.isEmpty() && !unique) {
            stats = repository.findStatsWithUri(start, end, uris);
        } else if (unique) {
            stats = repository.findStatsUnique(start, end);
        } else {
            stats = repository.findStats(start, end);
        }
        stats.sort(Comparator.reverseOrder());
        return stats;
    }

    @Override
    public void saveStat(EndpointHitDto hitDto) {
        log.info("StatService: выполнение запроса на добавление статистики");
        repository.save(hitMapper.toHit(hitDto));
    }
}
