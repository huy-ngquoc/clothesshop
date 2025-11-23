package vn.uit.clothesshop.feature.order.infra.jpa.projection;

public interface OrderStatisticByProduct {
    long getProductId();

    String getProductName();

    long getAmount();

    long getTotalPrice();
}
