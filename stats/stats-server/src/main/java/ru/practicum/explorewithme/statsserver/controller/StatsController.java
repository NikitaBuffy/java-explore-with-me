package ru.practicum.explorewithme.statsserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.statsdto.HitDto;
import ru.practicum.explorewithme.statsdto.StatsDto;
import ru.practicum.explorewithme.statsserver.service.StatsService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatsController {

    private final StatsService statsService;

    @PostMapping("/hit")
    public void addHit(@RequestBody HitDto hitDto) {
        log.info("Stats-Server. POST hit = {}", hitDto);
        statsService.addHit(hitDto);
    }

    @GetMapping("/stats")
    public List<StatsDto> getStats(@RequestParam String start,
                                   @RequestParam String end,
                                   @RequestParam(required = false) String[] uris,
                                   @RequestParam(defaultValue = "false") boolean unique) {
        log.info("Stats-Server. GET stats: start = {}, end = {}, uris = {}, unique = {}", start, end, uris, unique);
        return statsService.getStats(LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), uris, unique);
    }
}
