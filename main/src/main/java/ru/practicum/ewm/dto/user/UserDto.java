package ru.practicum.ewm.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    /*
     * Идентификатор
     */
    private Long id;

    /*
     * Почтовый адрес
     */
    private String email;

    /*
     * Имя
     */
    private String name;
}
