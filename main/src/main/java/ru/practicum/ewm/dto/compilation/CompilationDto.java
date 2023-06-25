package ru.practicum.ewm.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.dto.event.EventShortDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {

    /*
     * Идентификатор
     */
    private Long id;

    /*
     * Закреплена ли подборка на главной странице сайта
     */
    private Boolean pinned;

    /*
     * Заголовок подборки
     */
    private String title;

    /*
     * Список событий входящих в подборку
     */
    private List<EventShortDto> events;
}
