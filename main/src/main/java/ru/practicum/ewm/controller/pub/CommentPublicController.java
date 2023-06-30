package ru.practicum.ewm.controller.pub;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.comment.CommentDto;
import ru.practicum.ewm.service.comment.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class CommentPublicController {

    private final CommentService commentService;

    /*
     * Получение комментариев к событию с возможностью фильтрации
     * @param sort - вариант сортировки: по дате комментария, и по рейтингу (NEWEST_FIRST, POSITIVE_FIRST, NEGATIVE_FIRST)
     * @param from - количество комментариев, которые нужно пропустить для формирования текущего набора
     * @param size - количество комментариев в наборе
     * Возвращает List<CommentDto> - список найденных комментариев, либо код 400 (запрос составлен некорректно)
     */
    @GetMapping("/events/{eventId}/comments")
    public List<CommentDto> getComments(@PathVariable Long eventId,
                                        @RequestParam(defaultValue = "NEWEST_FIRST") String sort,
                                        @RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "10") int size) {
        return commentService.getComments(eventId, sort, from, size);
    }

    /*
     * Получение комментария к событию по его идентификатору
     * @param commentId - id комментария
     * Возвращает CommentDto - найденный комментарий, либо код 404 (комментарий не найден)
     */
    @GetMapping("/events/{eventId}/comments/{commentId}")
    public CommentDto getComment(@PathVariable Long eventId, @PathVariable Long commentId) {
        return commentService.getComment(eventId, commentId);
    }
}
