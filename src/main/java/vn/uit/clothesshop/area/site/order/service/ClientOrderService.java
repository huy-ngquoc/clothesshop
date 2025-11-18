package vn.uit.clothesshop.area.site.order.service;

import java.util.List;

import org.springframework.data.domain.Page;

import vn.uit.clothesshop.area.site.order.presentation.OrderRequestInfo;
import vn.uit.clothesshop.area.site.order.presentation.SingleOrderRequest;
import vn.uit.clothesshop.feature.cart.domain.Cart;
import vn.uit.clothesshop.feature.order.domain.Order;
import vn.uit.clothesshop.feature.order.domain.OrderDetail;
import vn.uit.clothesshop.feature.user.domain.User;

public interface ClientOrderService {
    public Order createOrderFromCart(long userId, OrderRequestInfo orderRequestInfo);
    public Order createSingleOrder(long userId,SingleOrderRequest request);
    public Order cancelOrder(long orderId, long userId);
    public Order confirmReceiveOrder(long orderId, long userId);
    public Page<Order> getOrders(long userId, int pageNumber, int size);
    public Order findOrderById(long userId, long orderId);
    public List<OrderDetail> findDetailsByOrderId(long userId, long orderId);
}
