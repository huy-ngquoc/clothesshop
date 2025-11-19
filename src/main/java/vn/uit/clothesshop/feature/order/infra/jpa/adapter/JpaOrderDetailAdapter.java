package vn.uit.clothesshop.feature.order.infra.jpa.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import vn.uit.clothesshop.feature.order.domain.OrderDetail;
import vn.uit.clothesshop.feature.order.domain.id.OrderDetailId;
import vn.uit.clothesshop.feature.order.domain.port.OrderDetailReadPort;
import vn.uit.clothesshop.feature.order.domain.port.OrderDetailWritePort;
import vn.uit.clothesshop.feature.order.infra.jpa.repository.OrderDetailRepository;

@Repository
public class JpaOrderDetailAdapter implements OrderDetailReadPort, OrderDetailWritePort {
    private final OrderDetailRepository orderDetailRepo;

    public JpaOrderDetailAdapter(OrderDetailRepository orderDetailRepo) {
        this.orderDetailRepo = orderDetailRepo;
    }

    @Override
    @NonNull
    public OrderDetail save(@NonNull OrderDetail orderDetail) {
        return orderDetailRepo.save(orderDetail);
    }

    @Override
    public void deleteById(@NonNull OrderDetailId id) {
        orderDetailRepo.deleteById(id);
    }

    @Override
    public void delete(@NonNull OrderDetail orderDetail) {
        orderDetailRepo.delete(orderDetail);
    }

    @Override
    public void saveAll(@NonNull List<OrderDetail> orderDetails) {
        orderDetailRepo.saveAll(orderDetails);
    }

    @Override
    public Page<OrderDetail> findAll(
            @Nullable Specification<OrderDetail> spec,
            @NonNull Pageable pageable) {
        return orderDetailRepo.findAll(spec, pageable);
    }

    @Override
    public Optional<OrderDetail> findById(@NonNull OrderDetailId id) {
        return orderDetailRepo.findById(id);
    }

    @Override
    public boolean existsById(@NonNull OrderDetailId id) {
        return orderDetailRepo.existsById(id);
    }

    @Override
    public Page<OrderDetail> findAllByOrderId(
            long orderId,
            @Nullable Specification<OrderDetail> spec,
            @NonNull Pageable pageable) {
        return orderDetailRepo.findAllByOrderId(
                orderId, spec, pageable);
    }

}
