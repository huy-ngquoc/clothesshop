package vn.uit.clothesshop.area.admin.dashboard.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.uit.clothesshop.feature.order.domain.Order;
import vn.uit.clothesshop.feature.order.domain.enums.EOrderStatus;
import vn.uit.clothesshop.feature.order.domain.port.OrderReadPort;
import vn.uit.clothesshop.feature.order.infra.jpa.projection.DailyIncomeStatistic;
import vn.uit.clothesshop.util.TimeConverter;

@Service
public class DashBoardServiceImplementation implements DashBoardService {
    private final OrderReadPort orderReadPort;

    public DashBoardServiceImplementation(OrderReadPort orderReadPort) {
        this.orderReadPort = orderReadPort;
    }

    @Override
    public Page<DailyIncomeStatistic> getDailyIncome(LocalDate from, LocalDate to, Pageable pageable) {
        final var instantFrom = TimeConverter.getInstantFromStartLocalDate(from);
        final var instantTo = TimeConverter.getInstantFromEndLocalDate(to);

        return this.orderReadPort.getDailyIncome(instantFrom, instantTo, EOrderStatus.RECEIVED, pageable);
    }

    @Override
    public long getIncomeOfMonth(final YearMonth yearMonth) {
        final var instantFrom = TimeConverter.getInstantFromStartLocalDate(yearMonth.atDay(1));
        final var instantTo = TimeConverter.getInstantFromEndLocalDate(yearMonth.atEndOfMonth());

        return this.orderReadPort.getIncomeByDateRange(instantFrom, instantTo, EOrderStatus.RECEIVED);
    }
    
    
}
