package ru.practicum.ewm.controller.priv;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.comment.CommentDto;
import ru.practicum.ewm.dto.comment.NewCommentDto;
import ru.practicum.ewm.service.comment.CommentService;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class CommentPrivateController {

    private final CommentService commentService;

    /*
     * Добавление нового комментария к событию
     * Нельзя прокомментировать и оставить рейтинг событию повторно
     * Комментарий может оставить только пользователь, который посетил мероприятие (имеет подтвержденный статус)
     * @param userId - id пользователя
     * @param eventId - id события
     * @param newCommentDto - данные добавляемого комментария
     * Возвращает CommentDto - новый комментарий, код 400 (запрос составлен некорректно),
     * либо 409 (нарушение целостности данных)
     */
    @PostMapping("/users/{userId}/events/{eventId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(@PathVariable Long userId, @PathVariable Long eventId,
                                 @Validated @RequestBody NewCommentDto newCommentDto) {
        return commentService.addComment(userId, eventId, newCommentDto);
    }

    /*
     * Удаление комментария к событию пользователем
     * @param userId - id пользователя
     * @param commentId - id комментария
     * Возвращает 204 при успешном удалении, код ошибки 404 (комментарий не найден),
     * либо код 409 (нарушение целостности данных)
     */
    @DeleteMapping("/users/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentByUser(@PathVariable Long userId, @PathVariable Long commentId) {
        commentService.deleteCommentByUser(userId, commentId);
    }
}
