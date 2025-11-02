package vn.uit.clothesshop.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.uit.clothesshop.order.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
