package vn.uit.clothesshop.area.admin.order.presentation.viewmodel;

import java.time.Instant;

import vn.uit.clothesshop.feature.order.domain.enums.EOrderStatus;

public final class OrderAdminBasicInfoViewModel {
    private final long id;
    private final long productPrice;
    private final long shippingFee;
    private final long total;
    private final EOrderStatus status;
    private final Instant createdAt;
    private final Instant updatedAt;

    public OrderAdminBasicInfoViewModel(
            long id,
            long productPrice,
            long shippingFee,
            long total,
            EOrderStatus status,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.productPrice = productPrice;
        this.shippingFee = shippingFee;
        this.total = total;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public long getId() {
        return id;
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

    public EOrderStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
