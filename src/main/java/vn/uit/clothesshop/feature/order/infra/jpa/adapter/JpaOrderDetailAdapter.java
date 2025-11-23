package vn.uit.clothesshop.feature.order.infra.jpa.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import vn.uit.clothesshop.feature.order.domain.OrderDetail;
import vn.uit.clothesshop.feature.order.domain.id.OrderDetailId;
import vn.uit.clothesshop.feature.order.domain.port.OrderDetailReadPort;
import vn.uit.clothesshop.feature.order.domain.port.OrderDetailWritePort;
import vn.uit.clothesshop.feature.order.infra.jpa.projection.OrderStatisticByCategory;
import vn.uit.clothesshop.feature.order.infra.jpa.projection.OrderStatisticByProduct;
import vn.uit.clothesshop.feature.order.infra.jpa.repository.OrderDetailRepository;

@Repository
class JpaOrderDetailAdapter implements OrderDetailReadPort, OrderDetailWritePort {
    private final OrderDetailRepository repo;

    public JpaOrderDetailAdapter(final OrderDetailRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<OrderDetail> findAll(
            @Nullable Specification<OrderDetail> spec,
            @NonNull Pageable pageable) {
        return repo.findAll(spec, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<OrderDetail> findById(@NonNull OrderDetailId id) {
        return repo.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsById(@NonNull OrderDetailId id) {
        return repo.existsById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<OrderDetail> findAllByOrderId(
            long orderId,
            @Nullable Specification<OrderDetail> spec,
            @NonNull Pageable pageable) {
        return repo.findAllByOrderId(
                orderId, spec, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<OrderDetail> findAllByOrderId(
            @NonNull final Iterable<Long> orderIds,
            @Nullable Specification<OrderDetail> spec,
            @NonNull Pageable pageable) {
        return this.repo.findAllByOrderId(orderIds, spec, pageable);
    }

    @Transactional
    @Override
    @NonNull
    public OrderDetail save(@NonNull OrderDetail orderDetail) {
        return repo.save(orderDetail);
    }

    @Transactional
    @Override
    public void deleteById(@NonNull OrderDetailId id) {
        repo.deleteById(id);
    }

    @Transactional
    @Override
    public void delete(@NonNull OrderDetail orderDetail) {
        repo.delete(orderDetail);
    }

    @Transactional
    @Override
    public void saveAll(@NonNull List<OrderDetail> orderDetails) {
        repo.saveAll(orderDetails);
    }

    @Override
    public Page<OrderStatisticByProduct> getStatisticByProduct(
            @Nullable Specification<OrderDetail> spec,
            @NonNull Pageable pageable) {
        return this.repo.getStatisticByProduct(spec, pageable);
    }

    @Override
    public Page<OrderStatisticByCategory> getStatisticByCategory(@Nullable Specification<OrderDetail> spec,
            @NonNull Pageable pageable) {
        return this.repo.getStatisticByCategory(spec, pageable);
    }

}
