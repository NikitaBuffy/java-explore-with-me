package ru.practicum.ewm.repository.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.exception.CategoryNotFoundException;
import ru.practicum.ewm.model.category.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    default Category getExistingCategory(Long catId) {
        return findById(catId).orElseThrow(() -> {
            throw new CategoryNotFoundException(String.format("Category with id=%d was not found", catId));
        });
    }
}
