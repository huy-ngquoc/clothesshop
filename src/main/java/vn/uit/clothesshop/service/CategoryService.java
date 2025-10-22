package vn.uit.clothesshop.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import vn.uit.clothesshop.domain.entity.Category;
import vn.uit.clothesshop.repository.CategoryRepository;

@Service
@Validated
public class CategoryService {
    @NotNull
    private final CategoryRepository categoryRepository;

    public CategoryService(@NotNull final CategoryRepository categoryRepo) {
        this.categoryRepository = categoryRepo;
    }

    @Nullable
    public Category findById(final long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    private void handleSaveCategory(@NotNull final Category category) {
        categoryRepository.save(category);
    }

    Category getReferenceById(final long id) {
        return this.categoryRepository.getReferenceById(id);
    }

    @Transactional
    boolean increaseAmountOfProduct(
            @Nullable final Long id,
            @PositiveOrZero final int amount) {
        return this.categoryRepository.increaseAmountOfProduct(id, amount) > 0;
    }

    @Transactional
    boolean decreaseAmountOfProduct(
            @Nullable final Long id,
            @PositiveOrZero final int amount) {
        return this.categoryRepository.decreaseAmountOfProduct(id, amount) > 0;
    }
}
