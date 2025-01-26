package ru.practicum.dto.location;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.model.location.Location;

@RequiredArgsConstructor
@Component
public class LocationMapper {

    public LocationDto toLocationDto(Location location) {
        return new LocationDto(
                location.getLat(),
                location.getLon()
        );
    }

    public Location toLocation(LocationDto locationDto, Integer id) {
        return new Location(
                id,
                locationDto.getLat(),
                locationDto.getLon()
        );
    }
}
