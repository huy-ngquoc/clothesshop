package vn.uit.clothesshop.area.admin.order.presentation.viewmodel;

import org.springframework.data.domain.Page;

import vn.uit.clothesshop.feature.order.domain.enums.EOrderStatus;

public final class OrderAdminDetailInfoViewModel {
    private final EOrderStatus status;
    private final long productPrice;
    private final long shippingFee;
    private final long total;
    private final Page<OrderDetailAdminViewModel> details;

    public OrderAdminDetailInfoViewModel(
            EOrderStatus status,
            long productPrice,
            long shippingFee,
            long total,
            Page<OrderDetailAdminViewModel> details) {
        this.status = status;
        this.productPrice = productPrice;
        this.shippingFee = shippingFee;
        this.total = total;
        this.details = details;
    }

    public EOrderStatus getStatus() {
        return status;
    }

    public long getProductPrice() {
        return productPrice;
    }

    public long getShippingFee() {
        return shippingFee;
    }

    public long getTotal() {
        return total;
    }

    public Page<OrderDetailAdminViewModel> getDetails() {
        return this.details;
    }

}
