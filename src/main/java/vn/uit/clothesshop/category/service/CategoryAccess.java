package vn.uit.clothesshop.category.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.extern.slf4j.Slf4j;
import vn.uit.clothesshop.category.domain.Category;
import vn.uit.clothesshop.category.repository.CategoryRepository;

@Service
@Transactional(readOnly = true)
@Validated
@Slf4j
public class CategoryAccess {

    private final CategoryRepository repository;

    public CategoryAccess(
            final CategoryRepository repository) {
        this.repository = repository;
    }

    public Page<Category> findAll(@NonNull final Pageable pageable) {
        return this.repository.findAll(pageable);
    }

    public Optional<Category> findById(final long id) {
        return this.repository.findById(id);
    }

    public void deleteById(final long id) {
        this.repository.deleteById(id);
    }

    @Transactional
    public boolean increaseProductCount(
            @Nullable final Long id,
            @PositiveOrZero final int amount) {
        return this.repository.decreaseProductCount(id, amount) > 0;
    }

    @Transactional
    public boolean decreaseProductCount(
            @Nullable final Long id,
            @PositiveOrZero final int amount) {
        return this.repository.decreaseProductCount(id, amount) > 0;
    }
}
