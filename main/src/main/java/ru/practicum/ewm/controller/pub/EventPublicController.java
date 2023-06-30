package ru.practicum.ewm.controller.pub;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.service.event.EventService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
public class EventPublicController {

    private final EventService eventService;

    /*
     * Получение событий с возможностью фильтрации
     * В выдаче должны быть только опубликованные события
     * Текстовый поиск (по аннотации и подробному описанию) должен быть без учета регистра букв
     * Если в запросе не указан диапазон дат, то выгружаются события, которые произойдут позже текущей даты и времени
     * Информация о каждом событии включает в себя количество просмотров и количество уже одобренных заявок на участие
     * Информация о том, что по этому эндпоинту был осуществлен и обработан запрос, сохраняется в сервисе статистики
     * В случае, если по заданным фильтрам не найдено ни одной категории, возвращает пустой список
     * @param rangeStart - дата и время не раньше которых должно произойти событие
     * @param onlyAvailable - только события у которых не исчерпан лимит запросов на участие
     * @param sort - Вариант сортировки: по дате события или по количеству просмотров (EVENT_DATE, VIEWS, RATING)
     * @param rangeEnd - дата и время не позже которых должно произойти событие
     * @param paid - поиск только платных/бесплатных событий
     * @param categories - список идентификаторов категорий в которых будет вестись поиск
     * @param text - текст для поиска в содержимом аннотации и подробном описании события
     * @param from - количество событий, которые нужно пропустить для формирования текущего набора
     * @param size - количество событий в наборе
     * Возвращает List<EventShortDto> - список найденных событий, либо код 400 (запрос составлен некорректно)
     */
    @GetMapping
    public List<EventShortDto> getEvents(@RequestParam(required = false) String text,
                                         @RequestParam(required = false) List<Long> categories,
                                         @RequestParam(required = false) Boolean paid,
                                         @RequestParam(required = false) String rangeStart,
                                         @RequestParam(required = false) String rangeEnd,
                                         @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                         @RequestParam(required = false) String sort,
                                         @RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "10") int size,
                                         HttpServletRequest request) {
        return eventService.getEvents(text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, sort, from, size, request);
    }

    /*
     * Получение подробной информации об опубликованном событии по его идентификатору
     * Событие должно быть опубликовано
     * Информация о событии включает в себя количество просмотров и количество подтвержденных запросов
     * Информация о том, что по этому эндпоинту был осуществлен и обработан запрос, сохраняется в сервисе статистики
     * @param eventId - id события
     * Возвращает EventFullDto - найденное событие, код 400 (запрос составлен некорректно),
     * либо код 404 (событие не найдено или недоступно)
     */
    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable Long eventId, HttpServletRequest request) {
        return eventService.getEvent(eventId, request);
    }
}
