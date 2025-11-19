package vn.uit.clothesshop.area.admin.statistic.service;

import java.time.LocalDate;

import vn.uit.clothesshop.area.admin.statistic.model.CompositeStatisticModel;

public interface StatisticService {
    public CompositeStatisticModel getStatistic(LocalDate from, LocalDate to);
}
