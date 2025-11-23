package vn.uit.clothesshop.area.admin.dashboard.service;

import java.time.LocalDate;
import java.time.YearMonth;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import vn.uit.clothesshop.feature.order.infra.jpa.projection.DailyIncomeStatistic;

public interface DashBoardService {
    Page<DailyIncomeStatistic> getDailyIncome(LocalDate from, LocalDate to, Pageable pageable);

    long getIncomeOfMonth(final YearMonth yearMonth);
}
