package ru.practicum.ewm.dto.request;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.model.request.Request;

import static ru.practicum.ewm.util.Constants.*;

@UtilityClass
public class RequestMapper {

    public static ParticipationRequestDto toRequestDto(Request request) {
        return new ParticipationRequestDto(
                request.getId(),
                request.getCreated().format(DATE_FORMAT),
                request.getEvent().getId(),
                request.getRequester().getId(),
                request.getStatus().toString()
        );
    }
}
