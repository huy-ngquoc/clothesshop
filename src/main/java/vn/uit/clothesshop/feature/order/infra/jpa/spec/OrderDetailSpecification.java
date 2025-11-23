package vn.uit.clothesshop.feature.order.infra.jpa.spec;

import java.time.Instant;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import vn.uit.clothesshop.feature.order.domain.Order;
import vn.uit.clothesshop.feature.order.domain.OrderDetail;

public final class OrderDetailSpecification {
    private OrderDetailSpecification() {
    }

    public static Specification<OrderDetail> orderIdEquals(
            final long orderId) {
        return (final Root<OrderDetail> root,
                final CriteriaQuery<?> _,
                final CriteriaBuilder criteriaBuilder) -> {
            final var orderIdPath = root.<Order>get(OrderDetail.Fields.order)
                    .<Long>get(Order.Fields.id);

            return criteriaBuilder.equal(orderIdPath, orderId);
        };
    }

    public static Specification<OrderDetail> orderCreatedBetween(
            @Nullable final Instant from,
            @Nullable final Instant to) {
        return (final Root<OrderDetail> root,
                final CriteriaQuery<?> _,
                final CriteriaBuilder criteriaBuilder) -> {
            final var orderCreatedAtPath = root.<Order>get(OrderDetail.Fields.order)
                    .<Instant>get(Order.Fields.createdAt);

            if (from == null) {
                if (to == null) {
                    return null;
                }

                return criteriaBuilder.lessThanOrEqualTo(orderCreatedAtPath, to);
            }

            if (to == null) {
                criteriaBuilder.greaterThanOrEqualTo(orderCreatedAtPath, from);
            }

            return criteriaBuilder.between(orderCreatedAtPath, from, to);
        };
    }
}
