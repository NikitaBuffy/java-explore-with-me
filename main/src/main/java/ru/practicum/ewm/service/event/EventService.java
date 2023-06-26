package ru.practicum.ewm.service.event;

import ru.practicum.ewm.dto.event.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {

    List<EventFullDto> getEventsByAdmin(List<Long> users, List<String> states, List<Long> categories, String rangeStart,
                                        String rangeEnd, int from, int size);

    EventFullDto editEventByAdmin(Long eventId, UpdateEventAdminRequestDto updateEventAdminRequestDto);

    List<EventShortDto> getEventsAddedByUser(Long userId, int from, int size);

    EventFullDto addEvent(Long userId, NewEventDto newEventDto);

    EventFullDto getEventAddedByUser(Long userId, Long eventId);

    EventFullDto editEventAddedByUser(Long userId, Long eventId, UpdateEventUserRequestDto updateEventUserRequestDto);

    List<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid, String rangeStart,
                                  String rangeEnd, boolean onlyAvailable, String sort, int from, int size,
                                  HttpServletRequest request);

    EventFullDto getEvent(Long eventId, HttpServletRequest request);
}
