package vn.uit.clothesshop.area.admin.dashboard.controller;

import java.time.LocalDate;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import vn.uit.clothesshop.area.admin.dashboard.service.DashBoardService;
import vn.uit.clothesshop.area.admin.statistic.service.StatisticService;
import vn.uit.clothesshop.area.shared.constant.ModelAttributeConstant;
import vn.uit.clothesshop.area.shared.constraint.PagingConstraint;
import vn.uit.clothesshop.feature.order.infra.jpa.projection.DailyIncomeStatistic;

@Controller
@RequiredArgsConstructor
public class DashboardAdminController {
    private final StatisticService statisticService;
    private final DashBoardService dashBoardService;
    private final ObjectMapper objectMapper;

    @GetMapping("/admin")
    public String getDashboard(
            Model model,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            @PageableDefault(size = PagingConstraint.DEFAULT_SIZE) Pageable pageable) {
        if (from == null) {
            from = LocalDate.now().minusDays(30);
        }
        if (to == null) {
            to = LocalDate.now();
        }

        final var stats = this.statisticService.getStatistic(from, to, pageable);
        model.addAttribute(ModelAttributeConstant.VIEW_MODEL, stats);

        final var dailyIncome = dashBoardService.getDailyIncome(from, to, pageable);
        final var categoryStats = stats.getStatisticByCategory().getContent();

        try {
            // Chuyển dữ liệu sang JSON String để JS đọc được
            model.addAttribute("chartDataIncome", objectMapper.writeValueAsString(dailyIncome.getContent()));
            model.addAttribute("chartDataCategory", objectMapper.writeValueAsString(categoryStats));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return "admin/dashboard/show";
    }

}
