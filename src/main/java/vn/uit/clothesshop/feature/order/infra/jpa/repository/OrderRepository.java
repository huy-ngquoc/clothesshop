package vn.uit.clothesshop.feature.order.infra.jpa.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.uit.clothesshop.feature.order.domain.Order;
import vn.uit.clothesshop.feature.order.domain.enums.EOrderStatus;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    public Page<Order> findByUser_Id(long userId, Pageable pageable);
    public List<Order> findByStatusAndCreatedAtBetween(EOrderStatus status, Instant from, Instant to);
}
