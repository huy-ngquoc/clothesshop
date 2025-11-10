package vn.uit.clothesshop.feature.category.infra.jpa.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import vn.uit.clothesshop.feature.category.domain.Category;
import vn.uit.clothesshop.feature.category.domain.port.CategoryReadPort;
import vn.uit.clothesshop.feature.category.domain.port.CategoryWritePort;
import vn.uit.clothesshop.feature.category.infra.jpa.repository.CategoryRepository;

@Repository
class JpaCategoryAdapter implements CategoryReadPort, CategoryWritePort {

    private final CategoryRepository repo;

    public JpaCategoryAdapter(
            final CategoryRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Category> findAll(
            @Nullable final Specification<Category> spec,
            @NonNull final Pageable pageable) {
        return this.repo.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> findRandom(final int size) {
        return this.repo.findRandom(PageRequest.of(0, size));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Category> findById(final long id) {
        return this.repo.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    @NotNull
    public Category getReferenceById(final long id) {
        return this.repo.getReferenceById(id);
    }

    @Override
    @Transactional
    public boolean increaseProductCount(
            @Nullable final Long id,
            @PositiveOrZero final int amount) {
        return this.repo.increaseProductCount(id, amount) > 0;
    }

    @Override
    @Transactional
    public boolean decreaseProductCount(
            @Nullable final Long id,
            @PositiveOrZero final int amount) {
        return this.repo.decreaseProductCount(id, amount) > 0;
    }

    @Override
    @Transactional
    @NonNull
    public Category save(@NonNull final Category category) {
        return this.repo.save(category);
    }

    @Override
    @Transactional
    public void deleteById(final long id) {
        this.repo.deleteById(id);
    }

    @Override
    @Transactional
    public void delete(@NonNull final Category category) {
        this.repo.delete(category);
    }
}
