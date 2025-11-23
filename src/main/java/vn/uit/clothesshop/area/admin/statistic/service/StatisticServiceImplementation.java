package vn.uit.clothesshop.area.admin.statistic.service;

import java.time.LocalDate;
import org.springframework.data.domain.Pageable;
import vn.uit.clothesshop.area.admin.statistic.model.CompositeStatisticModel;
import vn.uit.clothesshop.feature.order.domain.port.OrderDetailReadPort;
import vn.uit.clothesshop.feature.order.domain.port.OrderReadPort;
import vn.uit.clothesshop.util.TimeConverter;

public class StatisticServiceImplementation implements StatisticService {
    private final OrderReadPort orderReadPort;
    private final OrderDetailReadPort orderDetailReadPort;

    public StatisticServiceImplementation(
            OrderReadPort orderReadPort,
            OrderDetailReadPort orderDetailReadPort) {
        this.orderReadPort = orderReadPort;
        this.orderDetailReadPort = orderDetailReadPort;
    }

    @Override
    public CompositeStatisticModel getStatistic(LocalDate from, LocalDate to, Pageable pageable) {
        final var instantFrom = TimeConverter.getInstantFromStartLocalDate(from);
        final var instantTo = TimeConverter.getInstantFromEndLocalDate(to);

        final var amountOrders = this.orderReadPort.countByCreatedAtBetween(instantFrom, instantTo);
        final var totalProductPrice = this.orderDetailReadPort.getTotalProductPriceByOrderCreatedAtBetween(
                instantFrom, instantTo);
        final var statisticByProduct = this.orderDetailReadPort
                .getStatisticByProductAndOrderByCreatedAtBetween(
                        instantFrom, instantTo, pageable);
        final var statisticByCategory = this.orderDetailReadPort
                .getStatisticByCategoryAndOrderByCreatedAtBetween(
                        instantFrom, instantTo, pageable);

        return new CompositeStatisticModel(
                statisticByProduct,
                statisticByCategory,
                amountOrders,
                totalProductPrice);
    }
}
