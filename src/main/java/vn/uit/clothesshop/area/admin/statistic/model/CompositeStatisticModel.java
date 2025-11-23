package vn.uit.clothesshop.area.admin.statistic.model;

import org.springframework.data.domain.Page;

import vn.uit.clothesshop.feature.order.infra.jpa.projection.OrderStatisticByCategory;
import vn.uit.clothesshop.feature.order.infra.jpa.projection.OrderStatisticByProduct;

public class CompositeStatisticModel {
    private final Page<OrderStatisticByProduct> statisticByProduct;
    private final Page<OrderStatisticByCategory> statisticByCategory;
    private final long totalIncome;
    private final long totalOrders;

    public CompositeStatisticModel(
            final Page<OrderStatisticByProduct> statisticByProduct,
            final Page<OrderStatisticByCategory> statisticByCategory,
            final long totalIncome,
            final long totalOrders) {
        this.statisticByProduct = statisticByProduct;
        this.statisticByCategory = statisticByCategory;
        this.totalIncome = totalIncome;
        this.totalOrders = totalOrders;
    }

    public Page<OrderStatisticByProduct> getStatisticByProduct() {
        return statisticByProduct;
    }

    public Page<OrderStatisticByCategory> getStatisticByCategory() {
        return statisticByCategory;
    }

    public long getTotalIncome() {
        return totalIncome;
    }

    public long getTotalOrders() {
        return totalOrders;
    }

}
