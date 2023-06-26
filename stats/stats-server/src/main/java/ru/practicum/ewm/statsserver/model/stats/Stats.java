package ru.practicum.ewm.statsserver.model.stats;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Stats {

    private String app;

    private String uri;

    private Long hits;
}
