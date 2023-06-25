package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.category.NewCategoryDto;
import ru.practicum.ewm.service.category.CategoryService;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
public class CategoryAdminController {

    private final CategoryService categoryService;

    /*
     * Добавление новой категории
     * Имя категории должно быть уникальным
     * @param newCategoryDto - данные добавляемой категории
     * Возвращает CategoryDto - добавленную категорию, код 400 (запрос составлен некорректно),
     * либо 409 (нарушение целостности данных)
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@Validated @RequestBody NewCategoryDto newCategoryDto) {
        return categoryService.addCategory(newCategoryDto);
    }

    /*
     * Удаление категории
     * С категорией не должно быть связано ни одного события.
     * @param catId - id категории, которую нужно удалить
     * Возвращает 204 при успешном удалении, код 404 (категория не найдена или недоступна),
     * либо 409 (существуют события, связанные с категорией)
     */
    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        categoryService.deleteCategory(catId);
    }

    /*
     * Изменение категории
     * Имя категории должно быть уникальным
     * @param catId - id категории
     * @param newCategoryDto - данные категории для изменения
     * Возвращает CategoryDto - измененную категорию, код 404 (категория не найдена или недоступна),
     * либо 409 (нарушение целостности данных)
     */
    @PatchMapping("/{catId}")
    public CategoryDto editCategory(@PathVariable Long catId, @Validated @RequestBody NewCategoryDto newCategoryDto) {
        return categoryService.editCategory(catId, newCategoryDto);
    }

}
