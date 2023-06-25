package ru.practicum.ewm.controller.priv;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.service.request.RequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class RequestPrivateController {

    private final RequestService requestService;

    /*
     * Получение информации о заявках текущего пользователя на участие в чужих событиях
     * В случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой список
     * @param userId - id текущего пользователя
     * Возвращает List<ParticipationRequestDto> - список найденных запросов на участие, код 400 (запрос составлен некорректно),
     * либо 404 (пользователь не найден)
     */
    @GetMapping("/users/{userId}/requests")
    public List<ParticipationRequestDto> getUserRequestsForEvents(@PathVariable Long userId) {
        return requestService.getUserRequestsForEvents(userId);
    }

    /*
     * Добавление запроса от текущего пользователя на участие в событии
     * Нельзя добавить повторный запрос (Ожидается код ошибки 409)
     * Инициатор события не может добавить запрос на участие в своём событии (Ожидается код ошибки 409)
     * Нельзя участвовать в неопубликованном событии (Ожидается код ошибки 409)
     * Если у события достигнут лимит запросов на участие - необходимо вернуть ошибку (Ожидается код ошибки 409)
     * Если для события отключена пре-модерация запросов на участие, то запрос должен автоматически перейти в состояние подтвержденного
     * @param userId - id текущего пользователя
     * @param eventId - id события
     * Возвращает ParticipationRequestDto - созданную заявку на участие, код 400 (запрос составлен некорректно),
     * код 404 (Событие не найдено или недоступно), либо код 409 (нарушение целостности данных)
     */
    @PostMapping("/users/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createParticipationRequest(@PathVariable Long userId,
                                                              @RequestParam Long eventId) {
        return requestService.createParticipationRequest(userId, eventId);
    }

    /*
     * Отмена своего запроса на участие в событии
     * @param userId - id текущего пользователя
     * @param requestId - id запроса на участие
     * Возвращает ParticipationRequestDto - отмененную заявку на участие, либо код 404 (запрос не найден или недоступен)
     */
    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelParticipationRequest(@PathVariable Long userId,
                                                              @PathVariable Long requestId) {
        return requestService.cancelParticipationRequest(userId, requestId);
    }
}
