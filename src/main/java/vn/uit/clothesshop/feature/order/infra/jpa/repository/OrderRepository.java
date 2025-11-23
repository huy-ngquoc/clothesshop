package vn.uit.clothesshop.feature.order.infra.jpa.repository;

import java.time.Instant;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.micrometer.common.lang.NonNull;
import vn.uit.clothesshop.feature.order.domain.Order;
import vn.uit.clothesshop.feature.order.domain.enums.EOrderStatus;
import vn.uit.clothesshop.feature.order.infra.jpa.projection.DailyIncomeStatistic;
import vn.uit.clothesshop.feature.order.infra.jpa.projection.OrderStatisticByCategory;
import vn.uit.clothesshop.feature.order.infra.jpa.projection.OrderStatisticByProduct;

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
            WHERE o.createdAt BETWEEN :createdFrom AND :createdTo
            """)
    long countByCreatedAtBetween(
            @Param("createdFrom") @NonNull final Instant createdFrom,
            @Param("createdTo") @NonNull final Instant createdTo);

    @Query("""
            SELECT COALESCE(SUM(o.productPrice), 0)
            FROM Order o
            WHERE o.createdAt BETWEEN :createdFrom AND :createdTo
                AND o.status IN :statuses
            """)
    long getTotalProductPrice(
            @Param("createdFrom") @NonNull final Instant createdFrom,
            @Param("createdTo") @NonNull final Instant createdTo,
            @Param("statuses") @NonNull final Set<EOrderStatus> statuses);

    @Query("""
            SELECT COALESCE(SUM(o.shippingFee), 0)
            FROM Order o
            WHERE o.createdAt BETWEEN :createdFrom AND :createdTo
                AND o.status IN :statuses
            """)
    long getTotalShippingFee(
            @Param("createdFrom") @NonNull final Instant createdFrom,
            @Param("createdTo") @NonNull final Instant createdTo,
            @Param("statuses") @NonNull final Set<EOrderStatus> statuses);

    @Query("""
            SELECT COALESCE(SUM(o.totalPrice), 0)
            FROM Order o
            WHERE o.createdAt BETWEEN :createdFrom AND :createdTo
                AND o.status IN :statuses
            """)
    long getTotalPrice(
            @Param("createdFrom") @NonNull final Instant createdFrom,
            @Param("createdTo") @NonNull final Instant createdTo,
            @Param("statuses") @NonNull final Set<EOrderStatus> statuses);

    @Query(value = """
            SELECT
                p.id AS productId,
                p.name AS productName,
                COALESCE(SUM(od.amount), 0) AS amount,
                COALESCE(SUM(od.unitPrice * od.amount), 0) AS totalPrice
            FROM Order o
            JOIN o.details od
            JOIN od.productVariant pv
            JOIN pv.product p
            WHERE o.createdAt BETWEEN :createdFrom AND :createdTo
                AND o.status IN :statuses
            GROUP BY p.id, p.name
            """, countQuery = """
            SELECT COUNT(DISTINCT p.id)
            FROM OrderDetail od
            JOIN od.productVariant pv
            JOIN pv.product p
            JOIN od.order o
            WHERE o.createdAt BETWEEN :createdFrom AND :createdTo
                AND o.status IN :statuses
            """)
    Page<OrderStatisticByProduct> getStatisticByProduct(
            @Param("createdFrom") @NonNull final Instant createdFrom,
            @Param("createdTo") @NonNull final Instant createdTo,
            @Param("statuses") @NonNull final Set<EOrderStatus> statuses,
            @NonNull Pageable pageable);

    @Query(value = """
            SELECT
                c.id AS categoryId,
                c.name AS categoryName,
                COALESCE(SUM(od.amount), 0) AS amount,
                COALESCE(SUM(od.unitPrice * od.amount), 0) AS totalPrice
            FROM Order o
            JOIN o.details od
            JOIN od.productVariant pv
            JOIN pv.product p
            JOIN p.category c
            WHERE o.createdAt BETWEEN :createdFrom AND :createdTo
                AND o.status IN :statuses
            GROUP BY c.id, c.name
            """, countQuery = """
            SELECT COUNT(DISTINCT c.id)
            FROM OrderDetail od
            JOIN od.productVariant pv
            JOIN pv.product p
            JOIN p.category c
            JOIN od.order o
            WHERE o.createdAt BETWEEN :createdFrom AND :createdTo
                AND o.status IN :statuses
            """)
    Page<OrderStatisticByCategory> getStatisticByCategory(
            @Param("createdFrom") @NonNull final Instant createdFrom,
            @Param("createdTo") @NonNull final Instant createdTo,
            @Param("statuses") @NonNull final Set<EOrderStatus> statuses,
            @NonNull Pageable pageable);

    @Query("""
            SELECT COALESCE(SUM(o.totalPrice), 0)
            FROM Order o
            WHERE o.createdAt BETWEEN :createdFrom AND :createdTo
                AND o.status IN :statuses
            """)
    long getIncomeByDateRange(
            @Param("createdFrom") @NonNull final Instant createdFrom,
            @Param("createdTo") @NonNull final Instant createdTo,
            @Param("statuses") @NonNull final Set<EOrderStatus> statuses);

    @Query(value = """
            SELECT
                FUNCTION('DATE', o.createdAt) as date,
                COALESCE(SUM(o.totalPrice), 0) as totalIncome
            FROM Order o
            WHERE o.createdAt BETWEEN :createdFrom AND :createdTo
                AND o.status IN :statuses
            GROUP BY FUNCTION('DATE', o.createdAt)
            """, countQuery = """
            SELECT COUNT(DISTINCT FUNCTION('DATE', o.createdAt))
            FROM Order o
            WHERE o.createdAt BETWEEN :createdFrom AND :createdTo
                AND o.status IN :statuses
            """)
    Page<DailyIncomeStatistic> getDailyIncome(
            @Param("createdFrom") @NonNull final Instant createdFrom,
            @Param("createdTo") @NonNull final Instant createdTo,
            @Param("statuses") @NonNull final Set<EOrderStatus> statuses,
            @NonNull Pageable pageable);
}
