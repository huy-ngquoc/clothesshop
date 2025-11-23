package vn.uit.clothesshop.area.admin.statistic.service;

import java.time.LocalDate;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.uit.clothesshop.area.admin.statistic.model.CompositeStatisticModel;
import vn.uit.clothesshop.feature.order.domain.enums.EOrderStatus;
import vn.uit.clothesshop.feature.order.domain.port.OrderReadPort;
import vn.uit.clothesshop.util.TimeConverter;

@Service
public class StatisticServiceImplementation implements StatisticService {
    private final OrderReadPort orderReadPort;

    public StatisticServiceImplementation(
            OrderReadPort orderReadPort) {
        this.orderReadPort = orderReadPort;
    }

    @Override
    public CompositeStatisticModel getStatistic(LocalDate from, LocalDate to, Pageable pageable) {
        final var createdFrom = TimeConverter.getInstantFromStartLocalDate(from);
        final var createdTo = TimeConverter.getInstantFromEndLocalDate(to);

        final var amountOrders = this.orderReadPort.countByCreatedAtBetween(createdFrom, createdTo);
        final var totalProductPrice = this.orderReadPort.getTotalProductPrice(
                createdFrom, createdTo, EOrderStatus.RECEIVED);
        final var totalShippingFee = this.orderReadPort
                .getTotalShippingFee(createdFrom, createdTo, EOrderStatus.RECEIVED);
        final var statisticByProduct = this.orderReadPort
                .getStatisticByProduct(createdFrom, createdTo, EOrderStatus.RECEIVED, pageable);
        final var statisticByCategory = this.orderReadPort
                .getStatisticByCategory(createdFrom, createdTo, EOrderStatus.RECEIVED, pageable);

        return new CompositeStatisticModel(
                statisticByProduct,
                statisticByCategory,
                totalProductPrice + totalShippingFee,
                amountOrders);
    }
}
