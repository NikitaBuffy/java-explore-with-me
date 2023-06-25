package ru.practicum.ewm.controller.priv;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.dto.event.UpdateEventUserRequestDto;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateRequestDto;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateResultDto;
import ru.practicum.ewm.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.service.event.EventService;
import ru.practicum.ewm.service.request.RequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class EventPrivateController {

    private final EventService eventService;
    private final RequestService requestService;

    /*
     * Получение событий, добавленных текущим пользователем
     * В случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой список
     * @param userId - id текущего пользователя
     * @param from - количество элементов, которые нужно пропустить для формирования текущего набора
     * @param int - количество элементов в наборе
     * Возвращает List<EventShortDto> - список краткой информации о событиях, либо код 400
     */
    @GetMapping("/users/{userId}/events")
    public List<EventShortDto> getEventsAddedByUser(@PathVariable Long userId,
                                                      @RequestParam(defaultValue = "0") int from,
                                                      @RequestParam(defaultValue = "10") int size) {
        return eventService.getEventsAddedByUser(userId, from, size);
    }

    /*
     * Получение полной информации о событии добавленном текущим пользователем
     * В случае, если события с заданным id не найдено, возвращает статус код 404
     * @param userId - id текущего пользователя
     * @param eventId - id события
     * Возвращает EventFullDto - полную информацию о событии, код 400 (запрос составлен некорректно),
     * либо 404 (событие не найдено или недоступно)
     */
    @GetMapping("/users/{userId}/events/{eventId}")
    public EventFullDto getEventAddedByUser(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.getEventAddedByUser(userId, eventId);
    }

    /*
     * Добавление нового события
     * Дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента
     * @param userId - id текущего пользователя
     * @param newEventDto - данные добавляемого события
     * Возвращает EventFullDto - добавленное событие, код 400 (запрос составлен некорректно),
     * либо 409 (событие не удовлетворяет правилам создания)
     */
    @PostMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable Long userId, @Validated @RequestBody NewEventDto newEventDto) {
        return eventService.addEvent(userId, newEventDto);
    }

    /*
     * Изменение события, добавленного текущим пользователем
     * Изменить можно только отмененные события или события в состоянии ожидания модерации (ожидается код ошибки 409)
     * Дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента (ожидается код ошибки 409)
     * @param userId - id текущего пользователя
     * @param eventId - id события
     * @param updateEventUserRequestDto - новые данные события
     * Возвращает EventFullDto - полную информацию об обновленном событии, код 400 (запрос составлен некорректно),
     * код 404 (событие не найдено или недоступно), либо 409 (событие не удовлетворяет правилам редактирования)
     */
    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventFullDto editEventAddedByUser(@PathVariable Long userId, @PathVariable Long eventId,
                                               @Validated @RequestBody UpdateEventUserRequestDto updateEventUserRequestDto) {
        return eventService.editEventAddedByUser(userId, eventId, updateEventUserRequestDto);
    }

    /*
     * Получение информации о запросах на участие в событии текущего пользователя
     * В случае, если по заданным фильтрам не найдено ни одной заявки, возвращает пустой список
     * @param userId - id текущего пользователя
     * @param eventId - id события
     * Возвращает List<ParticipationRequestDto> - список найденных запросов на участие,
     * либо код 400 (запрос составлен некорректно),
     */
    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsForAddedEventByUser(@PathVariable Long userId,
                                                                          @PathVariable Long eventId) {
        return requestService.getRequestsForAddedEventByUser(userId, eventId);
    }

    /*
     * Изменение статуса (подтверждена, отменена) заявок на участие в событии текущего пользователя
     * Если для события лимит заявок равен 0 или отключена пре-модерация заявок, то подтверждение заявок не требуется
     * Нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие (Ожидается код ошибки 409)
     * Статус можно изменить только у заявок, находящихся в состоянии ожидания (Ожидается код ошибки 409)
     * Если при подтверждении данной заявки, лимит заявок для события исчерпан, то все неподтверждённые заявки необходимо отклонить
     * @param userId - id текущего пользователя
     * @param eventId - id события
     * @param eventRequestStatusUpdateRequestDto - новый статус для заявок на участие в событии текущего пользователя
     * Возвращает EventRequestStatusUpdateResultDto - cтатус заявок изменён, код 400 (запрос составлен некорректно),
     * код 404 (событие не найдено или недоступно), либо код 409 (достигнут лимит одобренных заявок)
     */
    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResultDto editRequestStatusForAddedEventByUser(@PathVariable Long userId, @PathVariable Long eventId,
                                                                                  @RequestBody EventRequestStatusUpdateRequestDto eventRequestStatusUpdateRequestDto) {
        return requestService.editRequestStatusForAddedEventByUser(userId, eventId, eventRequestStatusUpdateRequestDto);
    }

}
