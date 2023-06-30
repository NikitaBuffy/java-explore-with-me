package ru.practicum.ewm.service.comment;

import ru.practicum.ewm.dto.comment.CommentDto;
import ru.practicum.ewm.dto.comment.NewCommentDto;

import java.util.List;

public interface CommentService {

    CommentDto addComment(Long userId, Long eventId, NewCommentDto newCommentDto);

    void deleteCommentByUser(Long userId, Long commentId);

    void deleteCommentByAdmin(Long commentId);

    CommentDto getComment(Long eventId, Long commentId);

    List<CommentDto> getComments(Long eventId, String sort, int from, int size);

    List<CommentDto> getCommentsByAdmin(List<Long> events, String rangeStart, String rangeEnd, Integer rating, int from, int size);
}
