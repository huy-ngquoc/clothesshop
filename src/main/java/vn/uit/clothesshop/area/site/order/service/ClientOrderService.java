package vn.uit.clothesshop.area.site.order.service;

import vn.uit.clothesshop.area.site.order.presentation.OrderRequestInfo;
import vn.uit.clothesshop.area.site.order.presentation.SingleOrderRequest;
import vn.uit.clothesshop.feature.order.domain.Order;

public interface ClientOrderService {
    Order createOrderFromCart(long userId, OrderRequestInfo orderRequestInfo);

    Order createSingleOrder(long userId, SingleOrderRequest request);

    void cancelOrder(long orderId, long userId);

    void confirmReceiveOrder(long orderId, long userId);
}
