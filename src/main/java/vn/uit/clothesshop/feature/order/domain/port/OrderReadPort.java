package vn.uit.clothesshop.feature.order.domain.port;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import vn.uit.clothesshop.feature.order.domain.Order;
import vn.uit.clothesshop.feature.order.infra.jpa.projection.OrderTotalStatistic;

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

    default OrderTotalStatistic getTotalStatistic() {
        return this.getTotalStatistic(null);
    }

    OrderTotalStatistic getTotalStatistic(@Nullable Specification<Order> spec);
}
