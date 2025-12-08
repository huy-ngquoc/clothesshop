package vn.uit.clothesshop.area.admin.statistic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.uit.clothesshop.area.admin.statistic.service.StatisticService;

@Controller
@RequestMapping("/admin/statistic")
public class StatisticController {
    private final StatisticService statisticService;

    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

}
