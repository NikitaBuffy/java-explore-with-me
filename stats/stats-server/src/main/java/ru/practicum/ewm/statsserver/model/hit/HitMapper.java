package ru.practicum.ewm.statsserver.model.hit;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.statsdto.HitDto;
import ru.practicum.ewm.statsserver.util.Constants;

import java.time.LocalDateTime;

@UtilityClass
public class HitMapper {

    public static Hit dtoToHit(HitDto hitDto) {
        return new Hit(
                hitDto.getId(),
                hitDto.getApp(),
                hitDto.getUri(),
                hitDto.getIp(),
                LocalDateTime.parse(hitDto.getTimestamp(), Constants.DATE_FORMAT)
        );
    }
}
