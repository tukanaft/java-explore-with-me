package ru.practicum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.EndpointHitDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@Service
public class Client extends BaseClient {


    @Autowired
    public Client(@Value("${stat-server.url}") String statServerUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(statServerUrl))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {

        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique
        );
        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }

    public ResponseEntity<Object> saveStats(String app, String uris, String ip) {
        EndpointHitDto hitDto = EndpointHitDto.builder()
                .app(app)
                .uri(uris)
                .ip(ip)
                .timestamp(LocalDateTime.now())
                .build();
        return post("/hit", hitDto);
    }
}
