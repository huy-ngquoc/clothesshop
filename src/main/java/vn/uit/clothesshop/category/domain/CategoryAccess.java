package vn.uit.clothesshop.category.domain;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public interface CategoryAccess {
    default Page<Category> findAll(@NonNull final Pageable pageable) {
        return this.findAll(null, pageable);
    }

    Page<Category> findAll(
            @Nullable final Specification<Category> spec,
            @NonNull final Pageable pageable);

    Optional<Category> findById(final long id);

    @NotNull
    Category getReferenceById(final long id);

    boolean increaseProductCount(
            @Nullable final Long id,
            @PositiveOrZero final int amount);

    boolean decreaseProductCount(
            @Nullable final Long id,
            @PositiveOrZero final int amount);
}
