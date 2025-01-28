package ru.practicum.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.users.UserDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    Integer id;

    UserDto author;

    EventShortDto event;

    LocalDateTime created;

    String text;

    Boolean isPositive;
}
