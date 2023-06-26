package ru.practicum.ewm.statsserver.model.stats;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.statsdto.StatsDto;

@UtilityClass
public class StatsMapper {

    public static StatsDto statsToDto(Stats stats) {
        return new StatsDto(
                stats.getApp(),
                stats.getUri(),
                stats.getHits()
        );
    }
}
