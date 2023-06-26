package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.dto.compilation.UpdateCompilationRequestDto;
import ru.practicum.ewm.service.compilation.CompilationService;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/compilations")
public class CompilationAdminController {

    private final CompilationService compilationService;

    /*
     * Добавление новой подборки (подборка может не содержать событий)
     * @param newCompilationDto - данные новой подборки
     * Возвращает CompilationDto - добавленную подборку, код 400 (запрос составлен некорректно),
     * либо код 409 (нарушение целостности данных)
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilation(@Validated @RequestBody NewCompilationDto newCompilationDto) {
        return compilationService.addCompilation(newCompilationDto);
    }

    /*
     * Удаление подборки
     * @param compId - id подборки
     * Возвращает 204 при успешном удалении, код 404 (подборка не найдена или недоступна),
     */
    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        compilationService.deleteCompilation(compId);
    }

    /*
     * Обновление информации о подборке
     * @param compId - id подборки
     * @param updateCompilationRequestDto - данные для обновления подборки
     * Возвращает CompilationDto - обновленную подборку, либо код 404 (подборка не найдена или недоступна),
     */
    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(@PathVariable Long compId,
                                            @Validated @RequestBody UpdateCompilationRequestDto updateCompilationRequestDto) {
        return compilationService.updateCompilation(compId, updateCompilationRequestDto);
    }
}
