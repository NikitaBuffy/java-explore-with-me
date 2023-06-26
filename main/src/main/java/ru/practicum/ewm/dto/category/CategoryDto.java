package ru.practicum.ewm.dto.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    /*
     * Идентификатор категории
     */
    private Long id;

    /*
     * Название категории
     */
    private String name;
}
