package ru.practicum.ewm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateRequestDto {

    /*
     * Идентификаторы запросов на участие в событии текущего пользователя
     */
    private List<Long> requestIds;

    /*
     * Новый статус запроса на участие в событии текущего пользователя (CONFIRMED, REJECTED)
     */
    private String status;
}
