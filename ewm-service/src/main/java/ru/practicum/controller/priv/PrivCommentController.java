package ru.practicum.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.CommentUpdateRequest;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.service.comment.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/users/{userId}/comments")
public class PrivCommentController {
    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(@PathVariable Integer userId, @RequestBody NewCommentDto comment) {
        log.info("PrivateCommentController: выполнение запроса на добавление комментария");
        return commentService.addComment(userId, comment);
    }

    @PatchMapping("/{comId}")
    public CommentDto updateComment(@PathVariable Integer comId, @RequestBody CommentUpdateRequest comment) {
        log.info("PrivateCommentController: выполнение запроса на обновление комментария: {}", comId);
        return commentService.updateComment(comId, comment);
    }

    @GetMapping("/{eventId}")
    public List<CommentDto> getCommentsByAuthor(@PathVariable Integer userId,
                                                @RequestParam(required = false) Boolean isPositive,
                                                @RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "10") Integer size) {
        log.info("PrivateCommentController: выполнение запроса на отправление комментариев к событию");
        return commentService.getCommentsByAuthor(userId, isPositive, from, size);
    }

    @DeleteMapping("/{comId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Integer comId) {
        log.info("PrivateCommentController: выполнение запроса на удаление комментария: {}", comId);
        commentService.deleteComment(comId);
    }
}
