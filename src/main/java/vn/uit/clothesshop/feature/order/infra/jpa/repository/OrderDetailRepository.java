package vn.uit.clothesshop.feature.order.infra.jpa.repository;

import java.util.List;

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

@Repository
public interface OrderDetailRepository
        extends JpaRepository<OrderDetail, OrderDetailId>, JpaSpecificationExecutor<OrderDetail> {

    @Query("""
            SELECT od
            FROM OrderDetail od
            WHERE od.order.id = :orderId
            """)
    public Page<OrderDetail> findAllByOrderId(
            @Param("orderId") long orderId,
            @Nullable Specification<OrderDetail> spec,
            @NonNull Pageable pageable);
}
