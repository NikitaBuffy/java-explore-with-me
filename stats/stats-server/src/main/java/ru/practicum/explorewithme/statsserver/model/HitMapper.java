package ru.practicum.explorewithme.statsserver.model;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.statsdto.HitDto;

import java.time.LocalDateTime;

import static ru.practicum.explorewithme.statsserver.model.Constants.*;

@UtilityClass
public class HitMapper {

    public static Hit dtoToHit(HitDto hitDto) {
        return new Hit(
                hitDto.getId(),
                hitDto.getApp(),
                hitDto.getUri(),
                hitDto.getIp(),
                LocalDateTime.parse(hitDto.getTimestamp(), DATE_FORMAT)
        );
    }
}
