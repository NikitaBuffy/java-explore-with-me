package ru.practicum.ewm.statsdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatsDto {

    /*
    Название сервиса
    */
    private String app;

    /*
    URI сервиса
     */
    private String uri;

    /*
    Количество просмотров
    */
    private Long hits;
}
