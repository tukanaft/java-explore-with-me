package ru.practicum.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.service.comment.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/comments")
public class PubCommentController {
    private final CommentService commentService;

    @GetMapping("/events/{eventId}")
    public List<CommentDto> getCommentsByEvent(@PathVariable Integer eventId,
                                               @RequestParam(required = false) Boolean isPositive) {
        log.info("PubCommentController: выполнение запроса на отправление комментариев к событию");
        return commentService.getCommentsByEvent(eventId, isPositive);
    }

    @GetMapping
    public List<CommentDto> getCommentsBySearch(@RequestParam String text) {
        log.info("PubCommentController: выполнение запроса на отправление найденых комментариев по описанию");
        return commentService.getCommentsBySearch(text);
    }

    @GetMapping("/{comId}")
    public CommentDto getCommentById(@PathVariable Integer comId) {
        log.info("PubCommentController: выполнение запроса на отправление комментария");
        return commentService.getCommentById(comId);
    }
}
