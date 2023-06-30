package ru.practicum.ewm.service.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateRequestDto;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateResultDto;
import ru.practicum.ewm.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.dto.request.RequestMapper;
import ru.practicum.ewm.exception.ForbiddenException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.model.event.EventState;
import ru.practicum.ewm.model.request.Request;
import ru.practicum.ewm.model.request.RequestStatus;
import ru.practicum.ewm.model.user.User;
import ru.practicum.ewm.repository.event.EventRepository;
import ru.practicum.ewm.repository.request.RequestRepository;
import ru.practicum.ewm.repository.user.UserRepository;
import ru.practicum.ewm.util.PageRequestUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RequestServiceImpl extends PageRequestUtil implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public List<ParticipationRequestDto> getRequestsForAddedEventByUser(Long userId, Long eventId) {
        User user = userRepository.getExistingUser(userId);
        Event event = eventRepository.getExistingEvent(eventId);

        if (!event.getInitiator().equals(user)) {
            log.info("ParticipationRequest error. User with ID = {} can not view requests for event with ID = {}, " +
                    "because he is not an initiator", userId, eventId);
            throw new ForbiddenException("Event was not created by this user.");
        }

        return requestRepository.findByEventId(eventId)
                .stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventRequestStatusUpdateResultDto editRequestStatusForAddedEventByUser(Long userId, Long eventId,
                                                                                  EventRequestStatusUpdateRequestDto eventRequestStatusUpdateRequestDto) {
        userRepository.getExistingUser(userId);
        Event event = eventRepository.getExistingEvent(eventId);

        try {
            RequestStatus.valueOf(eventRequestStatusUpdateRequestDto.getStatus());
        } catch (IllegalArgumentException exception) {
            throw new ValidationException("Status doesn't exist.");
        }

        if (event.getParticipantLimit() == 0 || !event.isRequestModeration()) {
            log.info("Request error. For event with ID = {} participation limit = 0 or moderation is not required.", eventId);
            throw new ForbiddenException("Event doesn't have participation limit or doesn't require moderation.");
        }

        List<Request> requests = requestRepository.findAllByIdAndEventId(eventRequestStatusUpdateRequestDto.getRequestIds(), eventId);

        EventRequestStatusUpdateResultDto eventRequestStatusUpdateResultDto = new EventRequestStatusUpdateResultDto(
                new ArrayList<>(),
                new ArrayList<>()
        );

        if (!requests.isEmpty()) {
            if (RequestStatus.valueOf(eventRequestStatusUpdateRequestDto.getStatus()) == RequestStatus.CONFIRMED) {
                int participationLimit = event.getParticipantLimit();
                int currentParticipants = event.getConfirmedRequests();

                if (currentParticipants == participationLimit) {
                    log.info("Request error. Event with ID = {} has reached participant limit", eventId);
                    throw new ForbiddenException("Event has reached participant limit.");
                }
                for (Request request : requests) {
                    if (request.getStatus() != RequestStatus.PENDING) {
                        log.info("Request error. Request with ID = {} has status = {}. Must have PENDING.",
                                request.getId(), request.getStatus());
                        throw new ForbiddenException("Request must have pending status for confirmation.");
                    }
                    if (currentParticipants < participationLimit) {
                        request.setStatus(RequestStatus.CONFIRMED);
                        eventRequestStatusUpdateResultDto.getConfirmedRequests().add(RequestMapper.toRequestDto(request));
                        currentParticipants++;
                    } else {
                        request.setStatus(RequestStatus.REJECTED);
                        eventRequestStatusUpdateResultDto.getRejectedRequests().add(RequestMapper.toRequestDto(request));
                    }
                }
                requestRepository.saveAll(requests);
                event.setConfirmedRequests(currentParticipants);
                eventRepository.save(event);
            }
            if (RequestStatus.valueOf(eventRequestStatusUpdateRequestDto.getStatus()) == RequestStatus.REJECTED) {
                for (Request request : requests) {
                    if (request.getStatus() != RequestStatus.PENDING) {
                        log.info("Request error. Request with ID = {} has status = {}. Must have PENDING.",
                                request.getId(), request.getStatus());
                        throw new ForbiddenException("Request must have pending status for confirmation.");
                    }
                    request.setStatus(RequestStatus.REJECTED);
                    eventRequestStatusUpdateResultDto.getRejectedRequests().add(RequestMapper.toRequestDto(request));
                }
                requestRepository.saveAll(requests);
            }
        }

        return eventRequestStatusUpdateResultDto;
    }

    @Override
    public List<ParticipationRequestDto> getUserRequestsForEvents(Long userId) {
        userRepository.getExistingUser(userId);
        return requestRepository.findByRequesterId(userId)
                .stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto createParticipationRequest(Long userId, Long eventId) {
        User user = userRepository.getExistingUser(userId);
        Event event = eventRepository.getExistingEvent(eventId);

        Request request = requestRepository.findByRequesterIdAndEventId(userId, eventId);
        if (request != null) {
            log.info("ParticipationRequest error. User with ID = {} could not add request to event with ID = {}. " +
                    "User has already participated in the event!", userId, eventId);
            throw new ForbiddenException("User has already participated in the event.");
        }
        if (event.getInitiator().getId().equals(userId)) {
            log.info("ParticipationRequest error. User with ID = {} is an initiator of event with ID = {}", userId, eventId);
            throw new ForbiddenException("Initiator cannot add request to his own request.");
        }
        if (event.getState() != EventState.PUBLISHED) {
            log.info("ParticipationRequest error. Event with ID = {} is not published, " +
                    "requests for participation cannot be submitted.", eventId);
            throw new ForbiddenException("It is not possible to participate in an unpublished event.");
        }
        if (event.getParticipantLimit() != 0
                && requestRepository.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED) >= event.getParticipantLimit()) {
            log.info("ParticipationRequest error. Event with ID = {} has reached participation limit", eventId);
            throw new ForbiddenException("Event has reached participation limit.");
        }

        Request newRequest = new Request(null, LocalDateTime.now(), event, user, RequestStatus.PENDING);
        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            newRequest.setStatus(RequestStatus.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }

        ParticipationRequestDto addedRequest = RequestMapper.toRequestDto(requestRepository.save(newRequest));
        log.info("Created participation request = {}", addedRequest);
        return addedRequest;
    }

    @Override
    public ParticipationRequestDto cancelParticipationRequest(Long userId, Long requestId) {
        User user = userRepository.getExistingUser(userId);
        Request request = requestRepository.getExistingRequest(requestId);

        if (!request.getRequester().equals(user)) {
            log.info("ParticipationRequest error. User with ID = {} has not created request with ID = {}", userId, requestId);
            throw new ForbiddenException("Request was not created by this user.");
        }

        request.setStatus(RequestStatus.CANCELED);
        Request updatedRequest = requestRepository.save(request);

        return RequestMapper.toRequestDto(updatedRequest);
    }
}
