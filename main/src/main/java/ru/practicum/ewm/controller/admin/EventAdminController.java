package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.UpdateEventAdminRequestDto;
import ru.practicum.ewm.service.event.EventService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
public class EventAdminController {

    private final EventService eventService;

    /*
     * Эндпоинт возвращает полную информацию обо всех событиях подходящих под переданные условия
     * В случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой список
     * @param users - список id пользователей, чьи события нужно найти
     * @param states - список состояний, в которых находятся искомые события
     * @param categories - список id категорий в которых будет вестись поиск
     * @param rangeStart - дата и время, не раньше которых должно произойти событие
     * @param rangeEnd - дата и время, не позже которых должно произойти событие
     * @param from - количество событий, которые нужно пропустить для формирования текущего набора
     * @param int - количество событий в наборе
     * Возвращает List<EventFullDto> - список полной информации о событиях, либо код 400 (запрос составлен некорректно)
     */
    @GetMapping
    public List<EventFullDto> getEventsByAdmin(@RequestParam(required = false) List<Long> users,
                                               @RequestParam(required = false) List<String> states,
                                               @RequestParam(required = false) List<Long> categories,
                                               @RequestParam(required = false) String rangeStart,
                                               @RequestParam(required = false) String rangeEnd,
                                               @RequestParam(defaultValue = "0") int from,
                                               @RequestParam(defaultValue = "10") int size) {
        return eventService.getEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    /*
     * Редактирование данных события и его статуса (отклонение/публикация)
     * Валидация данных не требуется.
     * Дата начала изменяемого события должна быть не ранее чем за час от даты публикации (ожидается код ошибки 409)
     * Событие можно публиковать, только если оно в состоянии ожидания публикации (ожидается код ошибки 409)
     * Событие можно отклонить, только если оно еще не опубликовано (ожидается код ошибки 409)
     * @param eventId - id события
     * @param updateEventAdminRequestDto - данные для изменения информации о событии
     * Возвращает EventFullDto - отредактированное событие, код 404 (событие не найдено или недоступно),
     * либо код 409 (событие не удовлетворяет правилам редактирования)
     */
    @PatchMapping("/{eventId}")
    public EventFullDto editEventByAdmin(@PathVariable Long eventId, @Validated @RequestBody UpdateEventAdminRequestDto updateEventAdminRequestDto) {
        return eventService.editEventByAdmin(eventId, updateEventAdminRequestDto);
    }
}
