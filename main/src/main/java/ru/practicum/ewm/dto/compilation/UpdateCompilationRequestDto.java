package ru.practicum.ewm.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCompilationRequestDto {

    /*
     * Список событий входящих в подборку
     */
    private List<Long> events;

    /*
     * Закреплена ли подборка на главной странице сайта
     */
    private Boolean pinned;

    /*
     * Заголовок подборки
     */
    private String title;
}
