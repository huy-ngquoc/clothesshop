package vn.uit.clothesshop.feature.order.domain.port;

import java.util.List;

import org.springframework.lang.NonNull;

import vn.uit.clothesshop.feature.order.domain.Order;
import vn.uit.clothesshop.feature.order.domain.OrderDetail;
import vn.uit.clothesshop.feature.order.domain.id.OrderDetailId;

public interface OrderDetailWritePort {
     @NonNull
    OrderDetail save(@NonNull final OrderDetail orderDetail);

    void deleteById(OrderDetailId id);

    void delete(@NonNull OrderDetail orderDetail);

    void saveAll(final List<OrderDetail> orderDetails);
}
