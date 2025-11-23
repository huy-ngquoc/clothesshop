package vn.uit.clothesshop.feature.order.infra.jpa.projection;

public interface OrderTotalStatistic {
    long getAmount();

    long getTotalPrice();
}
