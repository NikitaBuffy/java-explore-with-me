package ru.practicum.ewm.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
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
    @Size(min = 1, max = 50)
    private String title;
}
