package vn.uit.clothesshop.area.admin.order.service;



import org.springframework.data.domain.Page;

import vn.uit.clothesshop.feature.order.domain.Order;

public interface AdminOrderService {
    public Order shipOrder(long userId, long orderId);
    public Order cancelOrder(long userId, long orderId);
    public Page<Order> getOrder(int pageNumber, int size);
    public Order getOrderById(long orderId);
}
