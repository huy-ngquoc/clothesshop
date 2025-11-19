package vn.uit.clothesshop.area.admin.order.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import vn.uit.clothesshop.area.admin.order.presentation.viewmodel.OrderAdminBasicInfoViewModel;
import vn.uit.clothesshop.area.admin.order.presentation.viewmodel.OrderAdminDetailInfoViewModel;
import vn.uit.clothesshop.feature.order.domain.Order;
import vn.uit.clothesshop.feature.order.domain.OrderDetail;

public interface OrderAdminService {
    default Page<OrderAdminBasicInfoViewModel> findAllBasic(
            @NonNull final Pageable pageable) {
        return this.findAllBasic(null, pageable);
    }

    Page<OrderAdminBasicInfoViewModel> findAllBasic(
            @Nullable final Specification<Order> spec,
            @NonNull final Pageable pageable);

    OrderAdminDetailInfoViewModel findDetailById(
            final long id,
            @Nullable Specification<OrderDetail> spec,
            @NonNull Pageable pageable);

    void shipOrder(long orderId);

    Order cancelOrder(long orderId);
}
