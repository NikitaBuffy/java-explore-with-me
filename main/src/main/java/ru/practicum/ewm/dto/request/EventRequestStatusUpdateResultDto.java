package ru.practicum.ewm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateResultDto {

    /*
     * Список подтвержденных заявок на участие в событии
     */
    private List<ParticipationRequestDto> confirmedRequests;

    /*
     * Список отклоненных заявок на участие в событии
     */
    private List<ParticipationRequestDto> rejectedRequests;
}
