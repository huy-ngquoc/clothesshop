package vn.uit.clothesshop.feature.order.infra.jpa.repository;

import java.time.Instant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.micrometer.common.lang.NonNull;
import vn.uit.clothesshop.feature.order.domain.Order;

@Repository
public interface OrderRepository extends
        JpaRepository<Order, Long>,
        JpaSpecificationExecutor<Order> {

    Page<Order> findByUser_Id(long userId, Pageable pageable);

    boolean existsByIdAndUser_Id(
            long id,
            long userId);

    @Query("""
            SELECT COUNT(o)
            FROM Order o
            WHERE o.createdAt BETWEEN :instantFrom AND :instantTo
            """)
    long countByCreatedAtBetween(
            @Param("instantFrom") @NonNull final Instant instantFrom,
            @Param("instantTo") @NonNull final Instant instantTo);
}
