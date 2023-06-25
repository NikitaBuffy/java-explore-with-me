package ru.practicum.ewm.service.request;

import ru.practicum.ewm.dto.request.EventRequestStatusUpdateRequestDto;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateResultDto;
import ru.practicum.ewm.dto.request.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    List<ParticipationRequestDto> getRequestsForAddedEventByUser(Long userId, Long eventId);

    EventRequestStatusUpdateResultDto editRequestStatusForAddedEventByUser(Long userId, Long eventId,
                                                                           EventRequestStatusUpdateRequestDto eventRequestStatusUpdateRequestDto);

    List<ParticipationRequestDto> getUserRequestsForEvents(Long userId);

    ParticipationRequestDto createParticipationRequest(Long userId, Long eventId);

    ParticipationRequestDto cancelParticipationRequest(Long userId, Long requestId);
}
