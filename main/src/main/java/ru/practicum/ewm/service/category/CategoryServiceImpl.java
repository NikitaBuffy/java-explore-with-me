package ru.practicum.ewm.service.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.category.CategoryMapper;
import ru.practicum.ewm.dto.category.NewCategoryDto;
import ru.practicum.ewm.exception.ForbiddenException;
import ru.practicum.ewm.model.category.Category;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.repository.category.CategoryRepository;
import ru.practicum.ewm.repository.event.EventRepository;
import ru.practicum.ewm.util.PageRequestUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl extends PageRequestUtil implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        Category category = categoryRepository.save(CategoryMapper.toCategory(newCategoryDto));
        log.info("Created new category: name = {}", category.getName());
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    public void deleteCategory(Long catId) {
        Category category = categoryRepository.getExistingCategory(catId);
        List<Event> events = eventRepository.findByCategoryId(catId);

        if (!events.isEmpty()) {
            log.info("Category error. Category with ID = {} cannot be deleted because it has linked events = {}", catId, events);
            throw new ForbiddenException("Cannot delete category because it's not empty.");
        }

        categoryRepository.delete(category);
        log.info("Deleted category with ID = {}", category.getId());
    }

    @Override
    public CategoryDto editCategory(Long catId, NewCategoryDto newCategoryDto) {
        categoryRepository.getExistingCategory(catId);
        Category categoryDataToUpdate = CategoryMapper.toCategory(newCategoryDto);
        categoryDataToUpdate.setId(catId);
        Category updatedCategory = categoryRepository.save(categoryDataToUpdate);
        return CategoryMapper.toCategoryDto(updatedCategory);
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        Pageable page = createPageRequest(from, size);

        return categoryRepository.findAll(page)
                .getContent()
                .stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {
        Category category = categoryRepository.getExistingCategory(catId);
        return CategoryMapper.toCategoryDto(category);
    }
}
