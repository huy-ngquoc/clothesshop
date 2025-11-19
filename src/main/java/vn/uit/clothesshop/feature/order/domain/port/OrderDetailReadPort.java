package vn.uit.clothesshop.feature.order.domain.port;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import vn.uit.clothesshop.feature.order.domain.Order;
import vn.uit.clothesshop.feature.order.domain.OrderDetail;
import vn.uit.clothesshop.feature.order.domain.id.OrderDetailId;

public interface OrderDetailReadPort {
    default Page<OrderDetail> findAll(@NonNull final Pageable pageable) {
        return this.findAll(null, pageable);
    }

    Page<OrderDetail> findAll(
            @Nullable final Specification<OrderDetail> spec,
            @NonNull final Pageable pageable);

    Optional<OrderDetail> findById(final OrderDetailId id);

    boolean existsById(final OrderDetailId id);
    public List<OrderDetail> findByOrderId(long orderId);
    public List<OrderDetail> findByOrderIdIn(List<Long> orderIds);
}

