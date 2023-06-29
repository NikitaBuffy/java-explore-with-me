package ru.practicum.ewm.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.location.LocationDto;
import ru.practicum.ewm.dto.user.UserShortDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventFullDto {

    /*
     * Идентификатор
     */
    private Long id;

    /*
     * Краткое описание
     */
    private String annotation;

    /*
     * Категория, к которой относится событие
     */
    private CategoryDto category;

    /*
     * Количество одобренных заявок на участие в данном событии
     */
    private int confirmedRequests;

    /*
     * Дата и время создания события (в формате "yyyy-MM-dd HH:mm:ss")
     */
    private String createdOn;

    /*
     * Полное описание события
     */
    private String description;

    /*
     * Дата и время, на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")
     */
    private String eventDate;

    /*
     * Пользователь (краткая информация)
     */
    private UserShortDto initiator;

    /*
     * Широта и долгота места проведения события
     */
    private LocationDto location;

    /*
     * Нужно ли оплачивать участие
     */
    private boolean paid;

    /*
     * Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
     */
    private int participantLimit;

    /*
     * Дата и время публикации события (в формате "yyyy-MM-dd HH:mm:ss")
     */
    private String publishedOn;

    /*
     * Нужна ли пре-модерация заявок на участие
     */
    private boolean requestModeration;

    /*
     * Список состояний жизненного цикла события (PENDING, PUBLISHED, CANCELED)
     */
    private String state;

    /*
     * Заголовок
     */
    private String title;

    /*
     * Количество просмотрев события
     */
    private Long views;

    /*
     * Рейтинг события
     */
    private double rating;
}
