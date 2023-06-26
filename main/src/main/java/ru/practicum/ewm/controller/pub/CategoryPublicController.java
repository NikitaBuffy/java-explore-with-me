package ru.practicum.ewm.controller.pub;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.service.category.CategoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/categories")
public class CategoryPublicController {

    private final CategoryService categoryService;

    /*
     * Получение категорий
     * В случае, если по заданным фильтрам не найдено ни одной категории, возвращает пустой список
     * @param from - количество категорий, которые нужно пропустить для формирования текущего набора
     * @param size - количество категорий в наборе
     * Возвращает List<CategoryDto> - список найденных категорий, либо код 400 (запрос составлен некорректно)
     */
    @GetMapping
    public List<CategoryDto> getCategories(@RequestParam(required = false, defaultValue = "0") int from,
                                           @RequestParam(required = false, defaultValue = "10") int size) {
        return categoryService.getCategories(from, size);
    }

    /*
     * Получение информации о категории по ее идентификатору
     * @param catId - id категории
     * Возвращает CategoryDto - найденную категорию, код 400 (запрос составлен некорректно),
     * либо код 404 (категория не найдена или недоступна)
     */
    @GetMapping("/{catId}")
    public CategoryDto getCategoryById(@PathVariable Long catId) {
        return categoryService.getCategoryById(catId);
    }
}
