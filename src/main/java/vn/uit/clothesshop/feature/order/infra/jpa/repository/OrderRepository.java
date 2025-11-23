package vn.uit.clothesshop.feature.order.infra.jpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import vn.uit.clothesshop.feature.order.domain.Order;
import vn.uit.clothesshop.feature.order.infra.jpa.projection.OrderTotalStatistic;

@Repository
public interface OrderRepository extends
        JpaRepository<Order, Long>,
        JpaSpecificationExecutor<Order> {

    Page<Order> findByUser_Id(long userId, Pageable pageable);

    boolean existsByIdAndUser_Id(
            long id,
            long userId);

    @Query("""
            SELECT
                COUNT(o) AS amount,
                SUM(o.total) AS totalPrice
            FROM Order o
            """)
    OrderTotalStatistic getTotalStatistic(@Nullable Specification<Order> spec);
}
