package ru.practicum.ewm.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.dto.location.LocationDto;
import ru.practicum.ewm.model.event.EventStateUserAction;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventUserRequestDto {

    /*
     * Краткое описание
     */
    @Size(min = 20, max = 2000)
    private String annotation;

    /*
     * Идентификатор категории, к которой относится событие
     */
    @Positive
    private Long category;

    /*
     * Полное описание события
     */
    @Size(min = 20, max = 7000)
    private String description;

    /*
     * Дата и время, на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")
     */
    private String eventDate;

    /*
     * Широта и долгота места проведения события
     */
    private LocationDto location;

    /*
     * Нужно ли оплачивать участие
     */
    private Boolean paid;

    /*
     * Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
     */
    private Integer participantLimit;

    /*
     * Нужна ли пре-модерация заявок на участие
     */
    private Boolean requestModeration;

    /*
     * Изменение состояния события (SEND_TO_REVIEW, CANCEL_REVIEW)
     */
    private EventStateUserAction stateAction;

    /*
     * Заголовок
     */
    @Size(min = 3, max = 120)
    private String title;
}
