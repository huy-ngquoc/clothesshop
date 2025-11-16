package vn.uit.clothesshop.area.site.order.service;

import java.util.List;

import vn.uit.clothesshop.area.site.order.presentation.OrderRequestInfo;
import vn.uit.clothesshop.area.site.order.presentation.SingleOrderRequest;
import vn.uit.clothesshop.feature.cart.domain.Cart;
import vn.uit.clothesshop.feature.order.domain.Order;
import vn.uit.clothesshop.feature.user.domain.User;

public interface ClientOrderService {
    public Order createOrderFromCart(long userId, OrderRequestInfo orderRequestInfo);
    public Order createSingleOrder(SingleOrderRequest request);
    public Order cancelOrder(long orderId);
    public Order confirmReceiveOrder(long orderId);
}
