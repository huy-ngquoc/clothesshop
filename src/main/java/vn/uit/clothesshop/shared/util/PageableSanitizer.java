package vn.uit.clothesshop.shared.util;

import java.util.Objects;
import java.util.Set;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;

import vn.uit.clothesshop.area.shared.constraint.PagingConstraint;

public final class PageableSanitizer {

    @NonNull
    private final Set<String> allowedSort;

    @NonNull
    private final String idFieldString;

    @SuppressWarnings("null")
    public PageableSanitizer(
            @NonNull final Set<String> allowedSort,
            @NonNull final String idFieldString) {
        this.allowedSort = Set.copyOf(allowedSort);
        this.idFieldString = idFieldString;
    }

    @SuppressWarnings("null")
    @NonNull
    public Set<String> getAllowedSort() {
        return Set.copyOf(allowedSort);
    }

    @NonNull
    public String getIdFieldString() {
        return this.idFieldString;
    }

    @SuppressWarnings("null")
    @NonNull
    public Pageable sanitize(@NonNull final Pageable pageable) {
        final var orders = pageable.getSort().stream().map((final var order) -> {
            final var property = order.getProperty();

            if (!this.allowedSort.contains(property)) {
                return null;
            }

            return new Sort.Order(order.getDirection(), property);
        }).filter(Objects::nonNull).toList();

        var mapped = Sort.by(orders);
        if (mapped.isUnsorted()) {
            mapped = Sort.by(Sort.Order.asc(this.idFieldString));
        } else if (mapped.stream().noneMatch(o -> o.getProperty().equals(this.idFieldString))) {
            mapped = mapped.and(Sort.by(Sort.Order.asc(this.idFieldString)));
        } else {
            // Sort is ready!
        }

        final var pageSize = Math.clamp(pageable.getPageSize(), PagingConstraint.MIN_SIZE, PagingConstraint.MAX_SIZE);
        return PageRequest.of(pageable.getPageNumber(), pageSize, mapped);
    }
}
