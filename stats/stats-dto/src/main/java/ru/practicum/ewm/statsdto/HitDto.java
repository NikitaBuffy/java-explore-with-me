package ru.practicum.ewm.statsdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HitDto {

    /*
    Идентификатор
    */
    private Long id;

    /*
    Идентификатор сервиса, для которого записывается информация
    */
    private String app;

    /*
    URI для которого был осуществлен запрос
     */
    private String uri;

    /*
    IP-адрес пользователя, осуществившего запрос
    */
    private String ip;

    /*
    Дата и время, когда был совершен запрос к эндпоинту (в формате "yyyy-MM-dd HH:mm:ss")
    */
    private String timestamp;
}
