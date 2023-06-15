package ru.practicum.explorewithme.statsserver.model;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.statsdto.HitDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class HitMapper {

//    public static HitDto hitToDto(Hit hit) {
//        return new HitDto(
//                hit.getId(),
//                hit.getApp(),
//                hit.getUri(),
//                hit.getIp(),
//                hit.getTimestamp().toString()
//        );
//    }

    public static Hit dtoToHit(HitDto hitDto) {
        return new Hit(
                hitDto.getId(),
                hitDto.getApp(),
                hitDto.getUri(),
                hitDto.getIp(),
                LocalDateTime.parse(hitDto.getTimestamp(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
    }
}
