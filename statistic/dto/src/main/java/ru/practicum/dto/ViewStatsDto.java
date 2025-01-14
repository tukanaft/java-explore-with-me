package ru.practicum.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ViewStatsDto implements Comparable<ViewStatsDto> {
    String app;
    String uri;
    Long hits;

    public ViewStatsDto(String app, String uri, Long hits) {
        this.app = app;
        this.uri = uri;
        this.hits = hits;
    }

    @Override
    public int compareTo(ViewStatsDto o) {
        return Long.compare(this.hits, o.hits);
    }
}
