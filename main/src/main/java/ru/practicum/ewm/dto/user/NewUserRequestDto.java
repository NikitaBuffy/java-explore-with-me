package ru.practicum.ewm.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewUserRequestDto {

    /*
     * Почтовый адрес
     */
    @NotBlank
    @Email
    @Size(min = 6, max = 254)
    private String email;

    /*
     * Имя
     */
    @NotBlank
    @Size(min = 2, max = 250)
    private String name;
}
