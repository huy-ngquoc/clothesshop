package vn.uit.clothesshop.feature.order.infra.jpa.adapter;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import vn.uit.clothesshop.feature.order.domain.Order;
import vn.uit.clothesshop.feature.order.domain.enums.EOrderStatus;
import vn.uit.clothesshop.feature.order.domain.port.OrderReadPort;
import vn.uit.clothesshop.feature.order.domain.port.OrderWritePort;
import vn.uit.clothesshop.feature.order.infra.jpa.projection.DailyIncomeStatistic;
import vn.uit.clothesshop.feature.order.infra.jpa.projection.OrderStatisticByCategory;
import vn.uit.clothesshop.feature.order.infra.jpa.projection.OrderStatisticByProduct;
import vn.uit.clothesshop.feature.order.infra.jpa.repository.OrderRepository;

@Repository
class JpaOrderAdapter implements OrderReadPort, OrderWritePort {

    private final OrderRepository repo;

    public JpaOrderAdapter(final OrderRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Order> findAll(
            @Nullable Specification<Order> spec,
            @NonNull Pageable pageable) {
        return this.repo.findAll(spec, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Order> findById(long id) {
        return this.repo.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsById(long id) {
        return this.repo.existsById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsByIdAndUserId(long id, long userId) {
        return this.repo.existsByIdAndUser_Id(id, userId);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Order> findAllByUserId(long userId, Pageable pageable) {
        return this.repo.findByUser_Id(userId, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public long countByCreatedAtBetween(
            @NonNull final Instant createdFrom,
            @NonNull final Instant createdTo) {
        return this.repo.countByCreatedAtBetween(createdFrom, createdTo);
    }

    @Transactional(readOnly = true)
    @Override
    public long getTotalShippingFee(
            @NonNull final Instant createdFrom,
            @NonNull final Instant createdTo,
            @NonNull final Set<EOrderStatus> statuses) {
        return this.repo.getTotalShippingFee(createdFrom, createdTo, statuses);
    }

    @Transactional(readOnly = true)
    @Override
    public long getTotalProductPrice(
            @NonNull final Instant createdFrom,
            @NonNull final Instant createdTo,
            @NonNull final Set<EOrderStatus> statuses) {
        return this.repo.getTotalProductPrice(createdFrom, createdTo, statuses);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<OrderStatisticByProduct> getStatisticByProduct(
            @NonNull final Instant createdFrom,
            @NonNull final Instant createdTo,
            @NonNull final Set<EOrderStatus> statuses,
            @NonNull Pageable pageable) {
        return this.repo.getStatisticByProduct(
                createdFrom, createdTo, statuses, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<OrderStatisticByCategory> getStatisticByCategory(
            @NonNull final Instant createdFrom,
            @NonNull final Instant createdTo,
            @NonNull final Set<EOrderStatus> statuses,
            @NonNull Pageable pageable) {
        return this.repo.getStatisticByCategory(
                createdFrom, createdTo, statuses, pageable);
    }

    @Transactional(readOnly = true)
    @Override

    public long getIncomeByDateRange(
            @NonNull final Instant createdFrom,
            @NonNull final Instant createdTo,
            @NonNull final Set<EOrderStatus> statuses) {
        return this.repo.getIncomeByDateRange(createdFrom, createdTo, statuses);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<DailyIncomeStatistic> getDailyIncome(
            @NonNull final Instant createdFrom,
            @NonNull final Instant createdTo,
            @NonNull final Set<EOrderStatus> statuses,
            @NonNull Pageable pageable) {
        return this.repo.getDailyIncome(createdFrom, createdTo, statuses, pageable);
    }

    @Transactional
    @Override
    @NonNull
    public Order save(@NonNull Order order) {
        return this.repo.save(order);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        this.repo.deleteById(id);
    }

    @Transactional
    @Override
    public void delete(@NonNull Order order) {
        this.repo.delete(order);
    }

}
