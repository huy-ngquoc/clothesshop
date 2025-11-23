package vn.uit.clothesshop.feature.order.infra.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vn.uit.clothesshop.feature.order.domain.OrderDetail;
import vn.uit.clothesshop.feature.order.domain.id.OrderDetailId;

@Repository
public interface OrderDetailRepository extends
        JpaRepository<OrderDetail, OrderDetailId>,
        JpaSpecificationExecutor<OrderDetail> {
}
