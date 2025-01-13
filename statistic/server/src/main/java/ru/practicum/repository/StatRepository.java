package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

@org.springframework.stereotype.Repository
public interface StatRepository extends JpaRepository<EndpointHit, Integer> {

    @Query("SELECT new ru.practicum.dto.ViewStatsDto(e.app, e.uri, COUNT(e.ip)) from EndpointHit e " +
            " Where e.timestamp Between ?1 and ?2" +
            " Group By e.app,e.uri")
    List<ViewStatsDto> findStats(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.dto.ViewStatsDto(e.app, e.uri, COUNT(DISTINCT e.ip)) from EndpointHit e " +
            " Where e.timestamp Between ?1 and ?2" +
            " Group By e.app,e.uri")
    List<ViewStatsDto> findStatsUnique(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.dto.ViewStatsDto(e.app, e.uri, COUNT(e.ip)) from EndpointHit e " +
            " Where e.timestamp Between ?1 and ?2" +
            " And e.uri in ?3" +
            " Group By e.app,e.uri")
    List<ViewStatsDto> findStatsWithUri(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.dto.ViewStatsDto(e.app, e.uri,COUNT(DISTINCT e.ip )) from EndpointHit e " +
            " Where e.timestamp Between ?1 and ?2" +
            " And e.uri in ?3" +
            " Group By e.app,e.uri")
    List<ViewStatsDto> findStatsWithUriUnique(LocalDateTime start, LocalDateTime end, List<String> uris);
}
