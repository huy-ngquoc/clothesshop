package vn.uit.clothesshop.category.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import vn.uit.clothesshop.category.domain.Category;
import vn.uit.clothesshop.category.domain.CategoryAccess;
import vn.uit.clothesshop.category.repository.CategoryRepository;

@Service
@Transactional(readOnly = true)
@Validated
class JpaCategoryAccess implements CategoryAccess {

    private final CategoryRepository repository;

    public JpaCategoryAccess(
            final CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Category> findAll(
            @Nullable final Specification<Category> spec,
            @NonNull final Pageable pageable) {
        return this.repository.findAll(spec, pageable);
    }

    @Override
    public Optional<Category> findById(final long id) {
        return this.repository.findById(id);
    }

    @Override
    @NotNull
    public Category getReferenceById(final long id) {
        return this.repository.getReferenceById(id);
    }

    @Override
    @Transactional
    public boolean increaseProductCount(
            @Nullable final Long id,
            @PositiveOrZero final int amount) {
        return this.repository.increaseProductCount(id, amount) > 0;
    }

    @Override
    @Transactional
    public boolean decreaseProductCount(
            @Nullable final Long id,
            @PositiveOrZero final int amount) {
        return this.repository.decreaseProductCount(id, amount) > 0;
    }
}
