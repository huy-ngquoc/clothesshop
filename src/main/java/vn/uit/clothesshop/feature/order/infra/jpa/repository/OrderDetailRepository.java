package vn.uit.clothesshop.feature.order.infra.jpa.repository;

import java.time.Instant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import vn.uit.clothesshop.feature.order.domain.OrderDetail;
import vn.uit.clothesshop.feature.order.domain.id.OrderDetailId;
import vn.uit.clothesshop.feature.order.infra.jpa.projection.OrderStatisticByCategory;
import vn.uit.clothesshop.feature.order.infra.jpa.projection.OrderStatisticByProduct;

@Repository
public interface OrderDetailRepository extends
        JpaRepository<OrderDetail, OrderDetailId>,
        JpaSpecificationExecutor<OrderDetail> {

    @Query("""
            SELECT COALESCE(SUM(od.unitPrice * od.amount), 0)
            FROM OrderDetail od
            JOIN od.order o
            WHERE o.id = :orderId
            """)
    long getProductPriceByOrderId(@Param("orderId") long orderId);

    @Query("""
            SELECT COALESCE(UM(od.unitPrice * od.amount), 0)
            FROM OrderDetail od
            JOIN od.order o
            WHERE o.createdAt BETWEEN :instantFrom AND :instantTo
            """)
    long getTotalProductPriceByOrderCreatedAtBetween(
            @Param("instantFrom") @NonNull final Instant instantFrom,
            @Param("instantTo") @NonNull final Instant instantTo);

    @Query(value = """
            SELECT
                p.id AS productId,
                p.name AS productName,
                COALESCE(SUM(od.amount), 0) AS amount,
                COALESCE(SUM(od.unitPrice * od.amount), 0) AS totalPrice
            FROM OrderDetail od
            JOIN od.order o
            JOIN od.productVariant pv
            JOIN pv.product p
            WHERE o.createdAt BETWEEN :instantFrom AND :instantTo
            GROUP BY p.id, p.name
            """, countQuery = """
            SELECT COUNT(DISTINCT p.id)
            FROM OrderDetail od
            JOIN od.productVariant pv
            JOIN pv.product p
            JOIN od.order o
            WHERE o.createdAt BETWEEN :instantFrom AND :instantTo
            """)
    Page<OrderStatisticByProduct> getStatisticByProductAndOrderByCreatedAtBetween(
            @Param("instantFrom") @NonNull final Instant instantFrom,
            @Param("instantTo") @NonNull final Instant instantTo,
            Pageable pageable);

    @Query(value = """
            SELECT
                c.id AS categoryId,
                c.name AS categoryName,
                COALESCE(SUM(od.amount), 0) AS amount,
                COALESCE(SUM(od.unitPrice * od.amount), 0) AS totalPrice
            FROM OrderDetail od
            JOIN od.order o
            JOIN od.productVariant pv
            JOIN pv.product p
            JOIN p.category c
            WHERE o.createdAt BETWEEN :instantFrom AND :instantTo
            GROUP BY c.id, c.name
            """, countQuery = """
            SELECT COUNT(DISTINCT c.id)
            FROM OrderDetail od
            JOIN od.productVariant pv
            JOIN pv.product p
            JOIN p.category c
            JOIN od.order o
            WHERE o.createdAt BETWEEN :instantFrom AND :instantTo
            """)
    Page<OrderStatisticByCategory> getStatisticByCategoryAndOrderByCreatedAtBetween(
            @Param("instantFrom") @NonNull final Instant instantFrom,
            @Param("instantTo") @NonNull final Instant instantTo,
            @NonNull Pageable pageable);
}
