package vn.uit.clothesshop.area.admin.dashboard.service;

import java.time.LocalDate;
import java.util.List;

public interface DashBoardService {
    public List<Long> incomeEveryDay(LocalDate from, LocalDate to);
    public Long incomeInMonth(int month, int year);
}
