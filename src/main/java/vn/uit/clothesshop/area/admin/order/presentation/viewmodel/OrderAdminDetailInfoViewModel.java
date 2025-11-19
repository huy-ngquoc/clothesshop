package vn.uit.clothesshop.area.admin.order.presentation.viewmodel;

import java.time.Instant;

import org.springframework.data.domain.Page;

import vn.uit.clothesshop.feature.order.domain.enums.EOrderStatus;

public final class OrderAdminDetailInfoViewModel {
    private final EOrderStatus status;
    private final long productPrice;
    private final long shippingFee;
    private final long total;
    private final String address;
    private final String phoneNumber;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final Page<OrderDetailAdminViewModel> details;

    public OrderAdminDetailInfoViewModel(
            EOrderStatus status,
            long productPrice,
            long shippingFee,
            String address,
            String phoneNumber,
            Instant createdAt,
            Instant updatedAt,
            Page<OrderDetailAdminViewModel> details) {
        this.status = status;
        this.productPrice = productPrice;
        this.shippingFee = shippingFee;
        this.total = productPrice + shippingFee;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Page<OrderDetailAdminViewModel> getDetails() {
        return this.details;
    }

}
