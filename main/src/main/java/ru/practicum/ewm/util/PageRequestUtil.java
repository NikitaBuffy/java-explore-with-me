package ru.practicum.ewm.util;

import org.springframework.data.domain.PageRequest;
import ru.practicum.ewm.model.comment.CommentSort;
import ru.practicum.ewm.model.event.EventSort;

public abstract class PageRequestUtil {

    protected PageRequest createPageRequest(int from, int size) {
        return PageRequest.of(from / size, size);
    }

    protected PageRequest createPageRequest(int from, int size, EventSort sort) {
        return PageRequest.of(from / size, size, sort.getSortValue());
    }

    protected PageRequest createPageRequest(int from, int size, CommentSort sort) {
        return PageRequest.of(from / size, size, sort.getSortValue());
    }
}
