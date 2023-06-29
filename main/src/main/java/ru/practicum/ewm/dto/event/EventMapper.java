package ru.practicum.ewm.dto.event;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.dto.category.CategoryMapper;
import ru.practicum.ewm.dto.location.LocationMapper;
import ru.practicum.ewm.dto.user.UserMapper;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.model.request.RequestStatus;

import java.time.LocalDateTime;

import static ru.practicum.ewm.util.Constants.*;

@UtilityClass
public class EventMapper {

    public static EventShortDto toEventShortDto(Event event) {
        return new EventShortDto(
                event.getId(),
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                event.getRequests() != null ? (int) event.getRequests().stream().filter(r -> r.getStatus() == RequestStatus.CONFIRMED).count() : 0,
                event.getEventDate().format(DATE_FORMAT),
                UserMapper.toUserShortDto(event.getInitiator()),
                event.isPaid(),
                event.getTitle(),
                event.getViews(),
                event.getComments() != null ? event.getComments().size() : 0,
                event.getRating()
        );
    }

    public static Event toEvent(NewEventDto newEventDto) {
        return new Event(
                null,
                newEventDto.getTitle(),
                newEventDto.getAnnotation(),
                null,
                newEventDto.getDescription(),
                null,
                null,
                null,
                LocalDateTime.parse(newEventDto.getEventDate(), DATE_FORMAT),
                null,
                newEventDto.getParticipantLimit(),
                newEventDto.isRequestModeration(),
                0,
                newEventDto.isPaid(),
                null,
                null,
                null,
                null,
                null,
                0.0
        );
    }

    public static EventFullDto toEventFullDto(Event event) {
        return new EventFullDto(
                event.getId(),
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                event.getRequests() != null ? (int) event.getRequests().stream().filter(r -> r.getStatus() == RequestStatus.CONFIRMED).count() : 0,
                event.getCreatedOn().format(DATE_FORMAT),
                event.getDescription(),
                event.getEventDate().format(DATE_FORMAT),
                UserMapper.toUserShortDto(event.getInitiator()),
                LocationMapper.toLocationDto(event.getLocation()),
                event.isPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn() != null ? event.getPublishedOn().format(DATE_FORMAT) : null,
                event.isRequestModeration(),
                event.getState().toString(),
                event.getTitle(),
                event.getViews(),
                event.getRating()
        );
    }
}
