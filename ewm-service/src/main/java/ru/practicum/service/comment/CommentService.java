package ru.practicum.service.comment;

import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.CommentUpdateRequest;
import ru.practicum.dto.comment.NewCommentDto;

import java.util.List;

public interface CommentService {
    CommentDto addComment(Integer userId, NewCommentDto comment);

    CommentDto updateComment(Integer comId, CommentUpdateRequest comment);

    List<CommentDto> getCommentsByAuthor(Integer userId, Boolean isPositive, Integer from, Integer size);

    void deleteComment(Integer comId);

    List<CommentDto> getCommentsByEvent(Integer eventId, Boolean isPositive);

    List<CommentDto> getCommentsBySearch(String text);

    CommentDto getCommentById(Integer comId);
}
