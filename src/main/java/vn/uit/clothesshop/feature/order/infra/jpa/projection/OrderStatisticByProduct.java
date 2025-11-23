package vn.uit.clothesshop.feature.order.infra.jpa.projection;

public interface OrderStatisticByProduct {
    long getProductId();

    long getProductName();

    long getAmount();

    long getTotalPrice();
}
