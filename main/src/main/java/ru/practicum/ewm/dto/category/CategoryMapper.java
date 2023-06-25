package ru.practicum.ewm.dto.category;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.model.category.Category;

@UtilityClass
public class CategoryMapper {

    public static Category toCategory(NewCategoryDto newCategoryDto) {
        return new Category(
                null,
                newCategoryDto.getName()
        );
    }

    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName()
        );
    }
}
