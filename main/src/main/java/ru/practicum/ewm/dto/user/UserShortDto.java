package ru.practicum.ewm.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserShortDto {

    /*
     * Идентификатор
     */
    private Long id;

    /*
     * Имя
     */
    private String name;
}
