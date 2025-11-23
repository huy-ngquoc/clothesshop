package vn.uit.clothesshop.area.admin.statistic.service;

import java.time.LocalDate;
import org.springframework.data.domain.Pageable;
import vn.uit.clothesshop.area.admin.statistic.model.CompositeStatisticModel;
import vn.uit.clothesshop.feature.order.domain.enums.EOrderStatus;
import vn.uit.clothesshop.feature.order.domain.port.OrderDetailReadPort;
import vn.uit.clothesshop.feature.order.domain.port.OrderReadPort;
import vn.uit.clothesshop.feature.order.infra.jpa.spec.OrderDetailSpecification;
import vn.uit.clothesshop.feature.order.infra.jpa.spec.OrderSpecification;
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

        final var orderSpec = OrderSpecification.createdBetween(instantFrom, instantTo)
                .and(OrderSpecification.hasStatus(EOrderStatus.RECEIVED));
        final var orderDetailSpec = OrderDetailSpecification.orderCreatedBetween(instantFrom, instantTo);

        final var totalIncomeAndOrders = this.orderReadPort.getTotalStatistic(orderSpec);
        final var statisticByProduct = this.orderDetailReadPort.getStatisticByProduct(orderDetailSpec, pageable);
        final var statisticByCategory = this.orderDetailReadPort.getStatisticByCategory(orderDetailSpec, pageable);

        return new CompositeStatisticModel(
                statisticByProduct,
                statisticByCategory,
                totalIncomeAndOrders.getAmount(),
                totalIncomeAndOrders.getTotalPrice());
    }
}
