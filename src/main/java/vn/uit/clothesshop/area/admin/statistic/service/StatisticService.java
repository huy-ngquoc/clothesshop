package vn.uit.clothesshop.area.admin.statistic.service;

import java.time.LocalDate;

import org.springframework.data.domain.Pageable;

import vn.uit.clothesshop.area.admin.statistic.model.CompositeStatisticModel;

public interface StatisticService {
    CompositeStatisticModel getStatistic(LocalDate from, LocalDate to, Pageable pageable);
}
