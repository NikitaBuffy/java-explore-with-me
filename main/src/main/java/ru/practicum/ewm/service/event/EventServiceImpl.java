package ru.practicum.ewm.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.event.*;
import ru.practicum.ewm.dto.location.LocationMapper;
import ru.practicum.ewm.exception.EventNotFoundException;
import ru.practicum.ewm.exception.ForbiddenException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.model.event.*;
import ru.practicum.ewm.model.location.Location;
import ru.practicum.ewm.repository.category.CategoryRepository;
import ru.practicum.ewm.repository.event.EventRepository;
import ru.practicum.ewm.repository.location.LocationRepository;
import ru.practicum.ewm.repository.user.UserRepository;
import ru.practicum.ewm.statsclient.StatsClient;
import ru.practicum.ewm.statsdto.HitDto;
import ru.practicum.ewm.statsdto.StatsDto;
import ru.practicum.ewm.util.PageRequestUtil;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.ewm.util.Constants.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl extends PageRequestUtil implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final StatsClient statsClient;

    @Override
    public List<EventFullDto> getEventsByAdmin(List<Long> users, List<String> states, List<Long> categories,
                                               String rangeStart, String rangeEnd, int from, int size) {
        Pageable page = createPageRequest(from, size);

        Page<Event> events = eventRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (users != null) {
                predicates.add(root.get("initiator").get("id").in(users));
            }
            if (states != null) {
                List<EventState> eventStates = new ArrayList<>();
                for (String state : states) {
                    try {
                        eventStates.add(EventState.valueOf(state));
                    } catch (IllegalArgumentException exception) {
                        throw new ValidationException("State doesn't exist.");
                    }
                }
                predicates.add(root.get("state").in(eventStates));
            }
            if (categories != null) {
                predicates.add(root.get("category").get("id").in(categories));
            }
            if (rangeStart != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), LocalDateTime.parse(rangeStart, DATE_FORMAT)));
            }
            if (rangeEnd != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), LocalDateTime.parse(rangeEnd, DATE_FORMAT)));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }, page);

        return events.getContent()
                .stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto editEventByAdmin(Long eventId, UpdateEventAdminRequestDto updateEventAdminRequestDto) {
        Event event = eventRepository.getExistingEvent(eventId);

        if (updateEventAdminRequestDto.getEventDate() != null) {
            LocalDateTime updatedEventDate = LocalDateTime.parse(updateEventAdminRequestDto.getEventDate(), DATE_FORMAT);
            checkEventDate(updatedEventDate);
            event.setEventDate(updatedEventDate);
        }

        EventStateAdminAction updateAdminState = updateEventAdminRequestDto.getStateAction();
        if (updateAdminState != null) {
            if (updateAdminState == EventStateAdminAction.PUBLISH_EVENT) {
                if (event.getState() != EventState.PENDING) {
                    log.info("Event error. Only pending events can be published by admin. Event ID = {}, state = {}",
                            event.getId(), event.getState());
                    throw new ForbiddenException("Cannot publish event. It should have pending state.");
                }
                if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
                    log.info("Event error. Event should be published no later than an hour before its start. " +
                            "Event date = {}, current time = {}", event.getEventDate(), LocalDateTime.now());
                    throw new ForbiddenException("Event cannot be published because there is less than an hour left before the event starts");
                } else {
                    event.setPublishedOn(LocalDateTime.now());
                    event.setState(EventState.PUBLISHED);
                }
            }
            if (updateAdminState == EventStateAdminAction.REJECT_EVENT) {
                if (event.getState() == EventState.PUBLISHED) {
                    log.info("Event error. Only unpublished events can be rejected by admin. Event ID = {}, state = {}",
                            event.getId(), event.getState());
                    throw new ForbiddenException("Cannot reject published event.");
                }
                if (event.getState() == EventState.CANCELED) {
                    log.info("Event error. Event is already canceled.");
                    throw new ForbiddenException("Event is already canceled.");
                }
                event.setState(EventState.CANCELED);
            }
        }

        if (updateEventAdminRequestDto.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminRequestDto.getAnnotation());
        }
        if (updateEventAdminRequestDto.getCategory() != null) {
            // Set category or throws CategoryNotFoundException
            event.setCategory(categoryRepository.getExistingCategory(updateEventAdminRequestDto.getCategory()));
        }
        if (updateEventAdminRequestDto.getDescription() != null) {
            event.setDescription(updateEventAdminRequestDto.getDescription());
        }
        if (updateEventAdminRequestDto.getLocation() != null) {
            Location location = event.getLocation();
            location.setLat(updateEventAdminRequestDto.getLocation().getLat());
            location.setLon(updateEventAdminRequestDto.getLocation().getLon());
            event.setLocation(location);
            locationRepository.save(location);
        }
        if (updateEventAdminRequestDto.getPaid() != null) {
            event.setPaid(updateEventAdminRequestDto.getPaid());
        }
        if (updateEventAdminRequestDto.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequestDto.getParticipantLimit());
        }
        if (updateEventAdminRequestDto.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminRequestDto.getRequestModeration());
        }
        if (updateEventAdminRequestDto.getTitle() != null) {
            event.setTitle(updateEventAdminRequestDto.getTitle());
        }

        Event updatedEvent = eventRepository.save(event);
        return EventMapper.toEventFullDto(updatedEvent);
    }

    @Override
    public List<EventShortDto> getEventsAddedByUser(Long userId, int from, int size) {
        userRepository.getExistingUser(userId);
        Pageable page = createPageRequest(from, size);

        return eventRepository.findByInitiatorId(userId, page)
                .getContent()
                .stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventAddedByUser(Long userId, Long eventId) {
        userRepository.getExistingUser(userId);
        // Check if user is an initiator of the event
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId).orElseThrow(() -> {
            throw new EventNotFoundException(String.format("Event with id=%d was not found", eventId));
        });
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto addEvent(Long userId, NewEventDto newEventDto) {
        userRepository.getExistingUser(userId);
        Event event = EventMapper.toEvent(newEventDto);

        checkEventDate(event.getEventDate());

        // Set initiator or throws UserNotFoundException
        event.setInitiator(userRepository.getExistingUser(userId));
        // Set category or throws CategoryNotFoundException
        event.setCategory(categoryRepository.getExistingCategory(newEventDto.getCategory()));
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);
        event.setLocation(locationRepository.save(LocationMapper.toLocation(newEventDto.getLocation())));
        event.setViews(0L);

        Event createdEvent = eventRepository.save(event);
        return EventMapper.toEventFullDto(createdEvent);
    }

    @Override
    public EventFullDto editEventAddedByUser(Long userId, Long eventId, UpdateEventUserRequestDto updateEventUserRequestDto) {
        userRepository.getExistingUser(userId);
        // Check if user is an initiator of the event
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId).orElseThrow(() -> {
            throw new EventNotFoundException(String.format("Event with id=%d was not found", eventId));
        });

        if (event.getState() != EventState.PENDING && event.getState() != EventState.CANCELED) {
            log.info("Only canceled or pending events can be edited. Event state = {}", event.getState());
            throw new ForbiddenException("Only canceled or pending events can be edited.");
        }

        if (updateEventUserRequestDto.getEventDate() != null) {
            LocalDateTime updatedEventDate = LocalDateTime.parse(updateEventUserRequestDto.getEventDate(), DATE_FORMAT);
            checkEventDate(updatedEventDate);
            event.setEventDate(updatedEventDate);
        }
        if (updateEventUserRequestDto.getAnnotation() != null) {
            event.setAnnotation(updateEventUserRequestDto.getAnnotation());
        }
        if (updateEventUserRequestDto.getCategory() != null) {
            // Set category or throws CategoryNotFoundException
            event.setCategory(categoryRepository.getExistingCategory(updateEventUserRequestDto.getCategory()));
        }
        if (updateEventUserRequestDto.getDescription() != null) {
            event.setDescription(updateEventUserRequestDto.getDescription());
        }
        if (updateEventUserRequestDto.getLocation() != null) {
            event.setLocation(LocationMapper.toLocation(updateEventUserRequestDto.getLocation()));
        }
        if (updateEventUserRequestDto.getPaid() != null) {
            event.setPaid(updateEventUserRequestDto.getPaid());
        }
        if (updateEventUserRequestDto.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventUserRequestDto.getParticipantLimit());
        }
        if (updateEventUserRequestDto.getRequestModeration() != null) {
            event.setRequestModeration(updateEventUserRequestDto.getRequestModeration());
        }
        EventStateUserAction updateUserState = updateEventUserRequestDto.getStateAction();
        if (updateUserState != null) {
            if (updateUserState == EventStateUserAction.SEND_TO_REVIEW) {
                event.setState(EventState.PENDING);
            }
            if (updateUserState == EventStateUserAction.CANCEL_REVIEW) {
                event.setState(EventState.CANCELED);
            }
        }
        if (updateEventUserRequestDto.getTitle() != null) {
            event.setTitle(updateEventUserRequestDto.getTitle());
        }

        Event updatedEvent = eventRepository.save(event);
        return EventMapper.toEventFullDto(updatedEvent);
    }

    @Override
    public List<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid, String rangeStart,
                                         String rangeEnd, boolean onlyAvailable, String sort, int from, int size,
                                         HttpServletRequest request) {
        if (rangeStart != null && rangeEnd != null &&
                LocalDateTime.parse(rangeStart, DATE_FORMAT).isAfter(LocalDateTime.parse(rangeEnd, DATE_FORMAT))) {
            log.info("Event error. Cannot get events because start is after end in requested parameters.");
            throw new ValidationException("Event start cannot be after event end.");
        }

        statsClient.addHit(new HitDto(
                null,
                "ewm-main-service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now().format(DATE_FORMAT)));

        Pageable page;
        if (sort != null) {
            page = createPageRequest(from, size, EventSort.valueOf(sort));
        } else {
            page = createPageRequest(from, size);
        }

        Page<Event> events = eventRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (text != null) {
                String searchText = "%" + text.toLowerCase() + "%";
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("annotation")), searchText),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), searchText)
                ));
            }
            if (categories != null) {
                predicates.add(root.get("category").get("id").in(categories));
            }
            if (paid != null) {
                if (paid) {
                    predicates.add(criteriaBuilder.isTrue(root.get("paid")));
                } else {
                    predicates.add(criteriaBuilder.isFalse(root.get("paid")));
                }
            }
            if (rangeStart == null && rangeEnd == null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), LocalDateTime.now()));
            } else {
                if (rangeStart != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), LocalDateTime.parse(rangeStart, DATE_FORMAT)));
                }
                if (rangeEnd != null) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), LocalDateTime.parse(rangeEnd, DATE_FORMAT)));
                }
            }
            if (onlyAvailable) {
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.equal(root.get("participantLimit"), 0),
                        criteriaBuilder.greaterThan(root.get("participantLimit"), criteriaBuilder.size(root.get("requests")))
                ));
            }
            predicates.add(criteriaBuilder.equal(root.get("state"), EventState.PUBLISHED));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }, page);

        return events.getContent()
                .stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEvent(Long eventId, HttpServletRequest request) {
        Event event = eventRepository.getExistingPublishedEvent(eventId, EventState.PUBLISHED);

        //Save request to stats-server
        statsClient.addHit(new HitDto(
                null,
                "ewm-main-service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now().format(DATE_FORMAT)));

        //Get unique event hits
        ResponseEntity<StatsDto[]> response = statsClient.getStats(LocalDateTime.now().minusYears(1).format(DATE_FORMAT),
                LocalDateTime.now().format(DATE_FORMAT), new String[]{request.getRequestURI()}, true);

        Long hits = 0L;
        if (response.getStatusCode().is2xxSuccessful()) {
            Optional<StatsDto> statDto = Arrays.stream(Objects.requireNonNull(response.getBody())).findFirst();
            if (statDto.isPresent()) {
                hits = statDto.get().getHits();
            }
        }

        event.setViews(hits);
        Event eventWithUpdatedViews = eventRepository.save(event);
        return EventMapper.toEventFullDto(eventWithUpdatedViews);
    }

    private void checkEventDate(LocalDateTime eventDate) {
        if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            log.info("Event error. Event date and time cannot be earlier than two hours from the current moment." +
                    "Event date = {}, current date = {}", eventDate, LocalDateTime.now());
            throw new ValidationException("Event date and time must be at least two hours from the current moment.");
        }
    }
}
