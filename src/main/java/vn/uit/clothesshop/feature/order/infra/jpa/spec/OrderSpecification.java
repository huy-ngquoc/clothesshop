package vn.uit.clothesshop.feature.order.infra.jpa.spec;

import java.time.Instant;
import java.util.Set;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import vn.uit.clothesshop.feature.order.domain.Order;
import vn.uit.clothesshop.feature.order.domain.enums.EOrderStatus;

public final class OrderSpecification {
    private OrderSpecification() {
    }

    public static Specification<Order> createdBetween(
            @Nullable final Instant from,
            @Nullable final Instant to) {
        return (final Root<Order> root,
                final CriteriaQuery<?> _,
                final CriteriaBuilder criteriaBuilder) -> {
            final var createdAtPath = root.<Instant>get(Order.Fields.createdAt);

            if (from == null) {
                if (to == null) {
                    return null;
                }

                return criteriaBuilder.lessThanOrEqualTo(createdAtPath, to);
            }

            if (to == null) {
                criteriaBuilder.greaterThanOrEqualTo(createdAtPath, from);
            }

            return criteriaBuilder.between(createdAtPath, from, to);
        };
    }

    public static Specification<Order> hasAnyStatuses(@Nullable final Set<EOrderStatus> statuses) {
        return (final Root<Order> root,
                final CriteriaQuery<?> _,
                final CriteriaBuilder criteriaBuilder) -> {
            if ((statuses == null) || (statuses.isEmpty())) {
                return null;
            }

            final var statusPath = root.<EOrderStatus>get(Order.Fields.status);
            final var statusIn = criteriaBuilder.in(statusPath);
            statuses.forEach(statusIn::value);

            return statusIn;
        };
    }

    public static Specification<Order> hasStatus(@NonNull final EOrderStatus status) {
        return (final Root<Order> root,
                final CriteriaQuery<?> _,
                final CriteriaBuilder criteriaBuilder) -> {
            final var statusPath = root.<EOrderStatus>get(Order.Fields.status);
            return criteriaBuilder.equal(statusPath, status);
        };
    }
}
