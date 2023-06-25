package ru.practicum.ewm.controller.pub;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.service.compilation.CompilationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/compilations")
public class CompilationPublicController {

    private final CompilationService compilationService;

    /*
     * Получение подборок событий
     * В случае, если по заданным фильтрам не найдено ни одной категории, возвращает пустой список
     * @param pinned - искать только закрепленные/не закрепленные подборки
     * @param from - количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size - количество элементов в наборе
     * Возвращает List<CompilationDto> - список найденных подборок событий, либо код 400 (запрос составлен некорректно)
     */
    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        return compilationService.getCompilations(pinned, from, size);
    }

    /*
     * Получение подборки событий по его id
     * @param compId - id подборки
     * Возвращает CompilationDto - подборку событий, код 400 (запрос составлен некорректно),
     * либо 404 (подборка не найдена или недоступна)
     */
    @GetMapping("/{compId}")
    public CompilationDto getCompilation(@PathVariable Long compId) {
        return compilationService.getCompilation(compId);
    }
}
