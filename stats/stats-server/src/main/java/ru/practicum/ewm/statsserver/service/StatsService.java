package ru.practicum.ewm.statsserver.service;

import ru.practicum.ewm.statsdto.HitDto;
import ru.practicum.ewm.statsdto.StatsDto;

import java.util.List;

public interface StatsService {

    void addHit(HitDto hitDto);

    List<StatsDto> getStats(String start, String end, String[] uris, boolean unique);
}
