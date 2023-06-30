package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.comment.CommentDto;
import ru.practicum.ewm.service.comment.CommentService;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/comments")
public class CommentAdminController {

    private final CommentService commentService;

    /*
     * Получение комментариев администратором с возможностью фильтрации
     * В случае, если по заданным фильтрам не найдено ни одного комментария, возвращает пустой список
     * @param events - список событий, для которых необходимо выгрузить комментарии
     * @param rangeStart - дата и время, не раньше которых был оставлен комментарий
     * @param rangeEnd - дата и время, не позже которых был оставлен комментарий
     * @param rating - рейтинг комментария (от 1 до 5)
     * @param from - количество комментариев, которые нужно пропустить для формирования текущего набора
     * @param size - количество комментариев в наборе
     * Возвращает List<CommentDto> - список найденных комментариев, либо код 400 (запрос составлен некорректно)
     */
    @GetMapping
    public List<CommentDto> getCommentsByAdmin(@RequestParam(required = false) List<Long> events,
                                        @RequestParam(required = false) String rangeStart,
                                        @RequestParam(required = false) String rangeEnd,
                                        @RequestParam(required = false) @Min(1) @Max(5) Integer rating,
                                        @RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "10") int size) {
        return commentService.getCommentsByAdmin(events, rangeStart, rangeEnd, rating, from, size);
    }

    /*
     * Удаление комментария к событию администратором
     * @param commentId - id комментария
     * Возвращает 204 при успешном удалении, код ошибки 404 (комментарий не найден)
     */
    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentByAdmin(@PathVariable Long commentId) {
        commentService.deleteCommentByAdmin(commentId);
    }
}
