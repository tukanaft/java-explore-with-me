package ru.practicum.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.CommentMapper;
import ru.practicum.dto.comment.CommentUpdateRequest;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.model.comment.Comment;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.State;
import ru.practicum.model.users.User;
import ru.practicum.repository.CommentRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@org.springframework.stereotype.Service
@Component
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    final CommentRepository commentRepository;
    final UserRepository userRepository;
    final EventRepository eventRepository;
    final CommentMapper commentMapper;

    @Override
    public CommentDto addComment(Integer userId, NewCommentDto comment) {
        log.info("CommentService: добавление комментария");
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("пользователя нет в базе"));
        Event event = eventRepository.findById(comment.getEvent())
                .orElseThrow(() -> new NotFoundException("события нет в базе"));
        Comment commentToSave = commentMapper.toComment(comment, user, event);
        commentValidation(commentToSave);
        return commentMapper.toCommentDto(commentRepository.save(commentToSave));
    }

    @Override
    public CommentDto updateComment(Integer comId, CommentUpdateRequest comment) {
        log.info("CommentService: обновление комментария");
        Comment commentSaved = getComment(comId);
        if (comment.getText() != null) {
            commentSaved.setText(comment.getText());
        }
        if (comment.getIsPositive() != null) {
            commentSaved.setIsPositive(comment.getIsPositive());
        }
        commentValidation(commentSaved);
        return commentMapper.toCommentDto(commentRepository.save(commentSaved));
    }

    @Override
    public List<CommentDto> getCommentsByAuthor(Integer userId, Boolean isPositive, Integer from, Integer size) {
        log.info("CommentService: отправление комментариев пользователя");
        List<Comment> comments = new ArrayList<>();
        if (isPositive == null) {
            comments = commentRepository.findAllByAuthor_Id(userId, PageRequest.of(from, size));
        } else {
            comments = commentRepository.findAllByAuthor_IdAndIsPositive(userId, isPositive, PageRequest.of(from, size));
        }
        return commentMapper.toCommentDtoList(comments);
    }

    @Override
    public void deleteComment(Integer comId) {
        log.info("CommentService: удаление комментария");
        getComment(comId);
        commentRepository.deleteById(comId);
    }

    @Override
    public List<CommentDto> getCommentsByEvent(Integer eventId, Boolean isPositive) {
        log.info("CommentService: отправление комментариев события");
        List<Comment> comments = new ArrayList<>();
        if (isPositive == null) {
            comments = commentRepository.findAllByEvent_Id(eventId);
        } else {
            comments = commentRepository.findAllByEvent_IdAndIsPositive(eventId, isPositive);
        }
        return commentMapper.toCommentDtoList(comments);
    }

    @Override
    public List<CommentDto> getCommentsBySearch(String text) {
        log.info("CommentService: отправление комментариев по описанию");
        return commentMapper.toCommentDtoList(commentRepository.findCommentsByText(text));
    }

    @Override
    public CommentDto getCommentById(Integer comId) {
        log.info("CommentService: отправление комментария");
        return commentMapper.toCommentDto(getComment(comId));
    }

    private void commentValidation(Comment comment) {
        String description = comment.getText();
        if (description == null || description.length() < 10 || description.length() > 2000 || description.isBlank()) {
            throw new ValidationException("не корректное описание в комментарии");
        }
        if (comment.getEvent().getState() != State.PUBLISHED) {
            throw new ConflictException("собфтие не опубликованно");
        }
    }

    private Comment getComment(Integer comId) {
        return commentRepository.findById(comId).orElseThrow(() -> new NotFoundException("комментария нет в базе"));
    }
}
