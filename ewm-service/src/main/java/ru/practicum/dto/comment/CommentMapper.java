package ru.practicum.dto.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.dto.event.EventMapper;
import ru.practicum.dto.users.UserMapper;
import ru.practicum.model.comment.Comment;
import ru.practicum.model.event.Event;
import ru.practicum.model.users.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class CommentMapper {
    final UserMapper userMapper;
    final EventMapper eventMapper;

    public CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                userMapper.toUserDto(comment.getAuthor()),
                eventMapper.toEventShortDto(comment.getEvent()),
                comment.getCreated(),
                comment.getText(),
                comment.getIsPositive()
        );
    }

    public Comment toComment(NewCommentDto commentDto, User author, Event event) {
        return new Comment(
                null,
                author,
                event,
                LocalDateTime.now(),
                commentDto.getText(),
                commentDto.getIsPositive()
        );
    }

    public List<CommentDto> toCommentDtoList(List<Comment> comments) {
        List<CommentDto> commentsDto = new ArrayList<>();
        for (Comment comment : comments) {
            commentsDto.add(toCommentDto(comment));
        }
        return commentsDto;
    }
}
