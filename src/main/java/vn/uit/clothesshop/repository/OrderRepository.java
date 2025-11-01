package vn.uit.clothesshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.uit.clothesshop.domain.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
