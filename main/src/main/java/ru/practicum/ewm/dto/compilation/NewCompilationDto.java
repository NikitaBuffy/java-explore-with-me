package ru.practicum.ewm.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewCompilationDto {

    /*
     * Заголовок подборки
     */
    @NotNull
    @NotBlank
    @Size(min = 1, max = 50)
    private String title;

    /*
     * Закреплена ли подборка на главной странице сайта
     * @default = false
     */
    private Boolean pinned = false;

    /*
     * Список событий входящих в подборку
     */
    private List<Long> events;
}
