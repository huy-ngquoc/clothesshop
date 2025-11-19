package vn.uit.clothesshop.feature.order.infra.jpa.adapter;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import vn.uit.clothesshop.area.shared.exception.OrderException;
import vn.uit.clothesshop.feature.order.domain.Order;
import vn.uit.clothesshop.feature.order.domain.enums.EOrderStatus;
import vn.uit.clothesshop.feature.order.domain.port.OrderReadPort;
import vn.uit.clothesshop.feature.order.domain.port.OrderWritePort;
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

    @Override
    public Page<Order> findAllByUserId(long userId, Pageable pageable) {
        return this.repo.findByUser_Id(userId, pageable);
    }

    @Override
    public Order findOrderDetailOfUser(long userId, long orderId) {
       Order order = this.repo.findById(orderId).orElseThrow(()->new OrderException("Order not found"));
       if(order.getUserId()!=userId) {
        throw new OrderException("You can not access this order");
       }
       return order;
    }

    @Override
    public List<Order> findByStatusAndCreatedAtBetween(EOrderStatus status, Instant from, Instant to) {
       return this.repo.findByStatusAndCreatedAtBetween(status, from, to);
    }
}
