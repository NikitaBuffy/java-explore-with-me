package ru.practicum.ewm.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.dto.location.LocationDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {

    /*
     * Краткое описание события
     */
    @NotNull
    @Size(min = 20, max = 2000)
    private String annotation;

    /*
     * id категории, к которой относится событие
     */
    @Positive
    private Long category;

    /*
     * Полное описание события
     */
    @NotNull
    @Size(min = 20, max = 7000)
    private String description;

    /*
     * Дата и время, на которые намечено событие. Дата и время указываются в формате "yyyy-MM-dd HH:mm:ss"
     */
    @NotNull
    private String eventDate;

    /*
     * Широта и долгота места проведения события
     */
    @NotNull
    private LocationDto location;

    /*
     * Нужно ли оплачивать участие в событии
     * @default = false
     */
    private boolean paid = false;

    /*
     * Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
     * @default = 0
     */
    private int participantLimit = 0;

    /*
     * Нужна ли пре-модерация заявок на участие
     * Если true, то все заявки будут ожидать подтверждения инициатором события
     * Если false - то будут подтверждаться автоматически
     * @default = true
     */
    private boolean requestModeration = true;

    /*
     * Заголовок события
     */
    @NotNull
    @Size(min = 3, max = 120)
    private String title;
}
