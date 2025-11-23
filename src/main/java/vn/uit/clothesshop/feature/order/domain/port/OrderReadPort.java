package vn.uit.clothesshop.feature.order.domain.port;

import java.time.Instant;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import vn.uit.clothesshop.feature.order.domain.Order;
import vn.uit.clothesshop.feature.order.domain.enums.EOrderStatus;
import vn.uit.clothesshop.feature.order.infra.jpa.projection.DailyIncomeStatistic;
import vn.uit.clothesshop.feature.order.infra.jpa.projection.OrderStatisticByCategory;
import vn.uit.clothesshop.feature.order.infra.jpa.projection.OrderStatisticByProduct;

public interface OrderReadPort {
    default Page<Order> findAll(@NonNull final Pageable pageable) {
        return this.findAll(null, pageable);
    }

    Page<Order> findAll(
            @Nullable final Specification<Order> spec,
            @NonNull final Pageable pageable);

    Optional<Order> findById(final long id);

    boolean existsById(final long id);

    boolean existsByIdAndUserId(final long id, final long userId);

    Page<Order> findAllByUserId(long userId, Pageable pageable);

    long countByCreatedAtBetween(
            @NonNull final Instant createdFrom,
            @NonNull final Instant createdTo);

    long getTotalShippingFee(
            @NonNull final Instant createdFrom,
            @NonNull final Instant createdTo,
            @NonNull final Set<EOrderStatus> statuses);

    default long getTotalShippingFee(
            @NonNull final Instant createdFrom,
            @NonNull final Instant createdTo,
            @NonNull final EOrderStatus status) {
        return this.getTotalShippingFee(createdFrom, createdTo, EnumSet.of(status));
    }

    long getTotalProductPrice(
            @NonNull final Instant createdFrom,
            @NonNull final Instant createdTo,
            @NonNull final Set<EOrderStatus> statuses);

    default long getTotalProductPrice(
            @NonNull final Instant createdFrom,
            @NonNull final Instant createdTo,
            @NonNull final EOrderStatus status) {
        return this.getTotalProductPrice(createdFrom, createdTo, EnumSet.of(status));
    }

    Page<OrderStatisticByProduct> getStatisticByProduct(
            @NonNull final Instant createdFrom,
            @NonNull final Instant createdTo,
            @NonNull final Set<EOrderStatus> statuses,
            @NonNull Pageable pageable);

    default Page<OrderStatisticByProduct> getStatisticByProduct(
            @NonNull final Instant createdFrom,
            @NonNull final Instant createdTo,
            @NonNull final EOrderStatus status,
            @NonNull Pageable pageable) {
        return this.getStatisticByProduct(createdFrom, createdTo, EnumSet.of(status), pageable);
    }

    Page<OrderStatisticByCategory> getStatisticByCategory(
            @NonNull final Instant createdFrom,
            @NonNull final Instant createdTo,
            @NonNull final Set<EOrderStatus> statuses,
            @NonNull Pageable pageable);

    default Page<OrderStatisticByCategory> getStatisticByCategory(
            @NonNull final Instant createdFrom,
            @NonNull final Instant createdTo,
            @NonNull final EOrderStatus status,
            @NonNull Pageable pageable) {
        return this.getStatisticByCategory(createdFrom, createdTo, EnumSet.of(status), pageable);
    }

    long getIncomeByDateRange(
            @NonNull final Instant createdFrom,
            @NonNull final Instant createdTo,
            @NonNull final Set<EOrderStatus> statuses);

    default long getIncomeByDateRange(
            @NonNull final Instant createdFrom,
            @NonNull final Instant createdTo,
            @NonNull final EOrderStatus status) {
        return this.getIncomeByDateRange(createdFrom, createdTo, EnumSet.of(status));
    }

    Page<DailyIncomeStatistic> getDailyIncome(
            @NonNull final Instant createdFrom,
            @NonNull final Instant createdTo,
            @NonNull final Set<EOrderStatus> statuses,
            @NonNull Pageable pageable);

    default Page<DailyIncomeStatistic> getDailyIncome(
            @NonNull final Instant createdFrom,
            @NonNull final Instant createdTo,
            @NonNull final EOrderStatus status,
            @NonNull Pageable pageable) {
        return this.getDailyIncome(createdFrom, createdTo, EnumSet.of(status), pageable);
    }
}
