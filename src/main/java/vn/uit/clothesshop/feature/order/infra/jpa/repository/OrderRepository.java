package vn.uit.clothesshop.feature.order.infra.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.uit.clothesshop.feature.order.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
