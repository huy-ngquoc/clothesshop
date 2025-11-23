package vn.uit.clothesshop.feature.order.infra.jpa.projection;

public interface OrderStatisticByCategory {
    long getCategoryId();

    String getCategoryName();

    long getAmount();

    long getTotalPrice();
}
