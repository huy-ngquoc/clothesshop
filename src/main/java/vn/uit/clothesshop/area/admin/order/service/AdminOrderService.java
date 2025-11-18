package vn.uit.clothesshop.area.admin.order.service;

import vn.uit.clothesshop.feature.order.domain.Order;

public interface AdminOrderService {
    public Order shipOrder(long userId, long orderId);
    public Order cancelOrder(long userId, long orderId);
}
