package vn.uit.clothesshop.feature.order.infra.jpa.repository;

import java.time.Instant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
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
            SELECT od
            FROM OrderDetail od
            WHERE od.order.id = :orderId
            """)
    Page<OrderDetail> findAllByOrderId(
            @Param("orderId") long orderId,
            @Nullable Specification<OrderDetail> spec,
            @NonNull Pageable pageable);

    @Query("""
            SELECT od
            FROM OrderDetail od
            WHERE od.order.id IN :orderIds
                """)
    Page<OrderDetail> findAllByOrderId(
            @Param("orderIds") Iterable<Long> orderIds,
            @Nullable Specification<OrderDetail> spec,
            @NonNull Pageable pageable);

    @Query("""
            SELECT
                p.id AS productId,
                p.name AS productName,
                SUM (od.amount) AS amount,
                SUM (od.priceCents * od.amount) AS totalPrice
            FROM OrderDetail od
            JOIN od.order o
            JOIN od.productVariants pv
            JOIN pv.product p
            GROUP BY p.id, p.name
            """)
    Page<OrderStatisticByProduct> getStatisticByProduct(
            @Nullable Specification<OrderDetail> spec,
            @NonNull Pageable pageable);

    @Query("""
            SELECT
                c.id AS categoryId,
                c.name AS categoryName,
                SUM (od.amount) AS amount,
                SUM (od.priceCents * od.amount) AS totalPrice
            FROM OrderDetail od
            JOIN od.order o
            JOIN od.productVariant pv
            JOIN pv.product p
            JOIN p.category c
            GROUP BY c.id, c.name
            """)
    Page<OrderStatisticByCategory> getStatisticByCategory(
            @Nullable Specification<OrderDetail> spec,
            @NonNull Pageable pageable);
}
