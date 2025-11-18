package vn.uit.clothesshop.area.admin.order.service;



import java.util.List;

import org.springframework.data.domain.Page;

import vn.uit.clothesshop.feature.order.domain.Order;
import vn.uit.clothesshop.feature.order.domain.OrderDetail;

public interface AdminOrderService {
    public Order shipOrder(long userId, long orderId);
    public Order cancelOrder(long userId, long orderId);
    public Page<Order> getOrder(int pageNumber, int size);
    public Order getOrderById(long orderId);
    public List<OrderDetail> getOrderDetails(long orderId);
}
