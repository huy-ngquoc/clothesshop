package vn.uit.clothesshop.feature.order.infra.jpa.projection;

import java.sql.Date;

public interface DailyIncomeStatistic {
    Date getDate();

    long getTotalIncome();
}
