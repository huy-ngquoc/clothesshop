package vn.uit.clothesshop.feature.order.domain.port;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import vn.uit.clothesshop.feature.order.domain.Order;
import vn.uit.clothesshop.feature.order.domain.enums.EOrderStatus;

public interface OrderReadPort {
    default Page<Order> findAll(@NonNull final Pageable pageable) {
        return this.findAll(null, pageable);
    }

    Page<Order> findAll(
            @Nullable final Specification<Order> spec,
            @NonNull final Pageable pageable);

    Optional<Order> findById(final long id);

    boolean existsById(final long id);
    public Page<Order> findAllByUserId(long userId, Pageable pageable);
    public Order findOrderDetailOfUser(long userId, long orderId);
    public List<Order> findByStatusAndCreatedAtBetween(EOrderStatus status, Instant from, Instant to);
}
