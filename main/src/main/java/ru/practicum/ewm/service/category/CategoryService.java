package ru.practicum.ewm.service.category;

import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.category.NewCategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto addCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(Long catId);

    CategoryDto editCategory(Long catId, NewCategoryDto newCategoryDto);

    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategoryById(Long catId);
}
