package ru.practicum.ewm.statsserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.statsdto.HitDto;
import ru.practicum.ewm.statsdto.StatsDto;
import ru.practicum.ewm.statsserver.service.StatsService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatsController {

    private final StatsService statsService;

    /*
     * Сохранение информации о том, что к эндпоинту был запрос
     * Имя категории должно быть уникальным
     * @param hitDto - данные запроса (название сервиса, URI, IP пользователя)
     */
    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void addHit(@RequestBody HitDto hitDto) {
        log.info("Stats-Server. POST hit = {}", hitDto);
        statsService.addHit(hitDto);
    }

    /*
     * Получение статистики по посещениям
     * @param start - дата и время начала диапазона, за который нужно выгрузить статистику (в формате "yyyy-MM-dd HH:mm:ss")
     * @param end - дата и время конца диапазона, за который нужно выгрузить статистику (в формате "yyyy-MM-dd HH:mm:ss")
     * @param uris - список uri для которых нужно выгрузить статистику
     * @param unique - нужно ли учитывать только уникальные посещения (только с уникальным ip)
     * Возвращает List<StatsDto> - список статистики по посещениям, либо код ошибки 400 (неверно составленный запрос)
     */
    @GetMapping("/stats")
    public List<StatsDto> getStats(@RequestParam String start,
                                   @RequestParam String end,
                                   @RequestParam(required = false) List<String> uris,
                                   @RequestParam(defaultValue = "false") boolean unique) {
        log.info("Stats-Server. GET stats: start = {}, end = {}, uris = {}, unique = {}", start, end, uris, unique);
        return statsService.getStats(start, end, uris, unique);
    }
}
