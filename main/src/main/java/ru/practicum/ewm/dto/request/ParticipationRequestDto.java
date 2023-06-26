package ru.practicum.ewm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationRequestDto {

    /*
     * Идентификатор заявки
     */
    private Long id;

    /*
     * Дата и время создания заявки
     */
    private String created;

    /*
     * Идентификатор события
     */
    private Long event;

    /*
     * Идентификатор пользователя, отправившего заявку
     */
    private Long requester;

    /*
     * Статус заявки
     */
    private String status;
}
