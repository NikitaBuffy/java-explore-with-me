package ru.practicum.ewm.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.user.UserShortDto;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventShortDto {

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
     * Дата и время, на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")
     */
    private String eventDate;

    /*
     * Пользователь (краткая информация)
     */
    private UserShortDto initiator;

    /*
     * Нужно ли оплачивать участие
     */
    private boolean paid;

    /*
     * Заголовок
     */
    private String title;

    /*
     * Количество просмотрев события
     */
    private Long views;

    /*
     * Количество комментариев к событию
     */
    private int comments;

    /*
     * Рейтинг события
     */
    private BigDecimal rating;
}
