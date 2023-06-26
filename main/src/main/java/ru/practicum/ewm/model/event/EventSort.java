package ru.practicum.ewm.model.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Getter
@RequiredArgsConstructor
public enum EventSort {

    EVENT_DATE(Sort.by(Sort.Direction.DESC, "eventDate")),
    VIEWS(Sort.by(Sort.Direction.DESC, "views"));

    private final Sort sortValue;
}
