package vn.uit.clothesshop.feature.order.domain.port;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import vn.uit.clothesshop.feature.order.domain.OrderDetail;
import vn.uit.clothesshop.feature.order.domain.id.OrderDetailId;
import vn.uit.clothesshop.feature.order.infra.jpa.projection.OrderStatisticByCategory;
import vn.uit.clothesshop.feature.order.infra.jpa.projection.OrderStatisticByProduct;

public interface OrderDetailReadPort {
    default Page<OrderDetail> findAll(@NonNull final Pageable pageable) {
        return this.findAll(null, pageable);
    }

    Page<OrderDetail> findAll(
            @Nullable final Specification<OrderDetail> spec,
            @NonNull final Pageable pageable);

    default Page<OrderDetail> findAllByOrderId(
            @NonNull final Iterable<Long> orderIds,
            @NonNull Pageable pageable) {
        return this.findAllByOrderId(orderIds, null, pageable);
    }

    Page<OrderDetail> findAllByOrderId(
            @NonNull final Iterable<Long> orderIds,
            @Nullable Specification<OrderDetail> spec,
            @NonNull Pageable pageable);

    default Page<OrderDetail> findAllByOrderId(
            long orderId,
            @NonNull Pageable pageable) {
        return this.findAllByOrderId(orderId, null, pageable);
    }

    Page<OrderDetail> findAllByOrderId(
            long orderId,
            @Nullable Specification<OrderDetail> spec,
            @NonNull Pageable pageable);

    Optional<OrderDetail> findById(@NonNull final OrderDetailId id);

    boolean existsById(@NonNull final OrderDetailId id);

    long getProductPriceByOrderId(final long orderId);

    default Page<OrderStatisticByProduct> getStatisticByProduct(
            @NonNull Pageable pageable) {
        return this.getStatisticByProduct(null, pageable);
    }

    Page<OrderStatisticByProduct> getStatisticByProduct(
            @Nullable Specification<OrderDetail> spec,
            @NonNull Pageable pageable);

    default Page<OrderStatisticByCategory> getStatisticByCategory(
            @NonNull Pageable pageable) {
        return this.getStatisticByCategory(null, pageable);
    }

    Page<OrderStatisticByCategory> getStatisticByCategory(
            @Nullable Specification<OrderDetail> spec,
            @NonNull Pageable pageable);
}
