package vn.uit.clothesshop.area.site.order.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

import vn.uit.clothesshop.area.site.order.presentation.OrderRequestInfo;
import vn.uit.clothesshop.area.site.order.presentation.SingleOrderRequest;
import vn.uit.clothesshop.feature.order.domain.Order;
import vn.uit.clothesshop.feature.order.domain.OrderDetail;

public interface ClientOrderService {
    Order createOrderFromCart(long userId, OrderRequestInfo orderRequestInfo);

    Order createSingleOrder(long userId, SingleOrderRequest request);

    void cancelOrder(long orderId, long userId);

    void confirmReceiveOrder(long orderId, long userId);

    Page<Order> getOrders(long userId, int pageNumber, int size);

    Order findOrderById(long id);

    Page<OrderDetail> findDetailsByOrderId(
            long id,
            @NonNull Pageable pageable);
}
