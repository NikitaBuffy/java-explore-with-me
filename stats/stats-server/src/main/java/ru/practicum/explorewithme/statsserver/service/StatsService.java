package ru.practicum.explorewithme.statsserver.service;

import ru.practicum.explorewithme.statsdto.HitDto;
import ru.practicum.explorewithme.statsdto.StatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    void addHit(HitDto hitDto);

    List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique);
}
