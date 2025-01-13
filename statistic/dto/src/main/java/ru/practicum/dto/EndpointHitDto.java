package ru.practicum.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class EndpointHitDto {
    Integer id;
    String app;
    String uri;
    String ip;
    LocalDateTime timestamp;

    public EndpointHitDto(Integer id, String app, String uri, String ip, LocalDateTime timestamp) {
        this.id = id;
        this.app = app;
        this.uri = uri;
        this.ip = ip;
        this.timestamp = timestamp;
    }
}
