package ru.practicum.ewm.model.comment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Getter
@RequiredArgsConstructor
public enum CommentSort {

    NEWEST_FIRST(Sort.by(Sort.Direction.DESC, "created")),
    NEGATIVE_FIRST(Sort.by(Sort.Direction.ASC, "rating")),
    POSITIVE_FIRST(Sort.by(Sort.Direction.DESC, "rating"));

    private final Sort sortValue;
}
