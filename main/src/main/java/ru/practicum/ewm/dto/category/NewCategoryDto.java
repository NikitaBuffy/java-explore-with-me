package ru.practicum.ewm.dto.category;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewCategoryDto {

    /*
     * Название категории
     */
    @NotBlank
    @Size(min = 1, max = 50)
    private String name;
}
