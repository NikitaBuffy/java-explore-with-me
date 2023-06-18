package ru.practicum.explorewithme.statsserver.model;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.statsdto.StatsDto;

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
