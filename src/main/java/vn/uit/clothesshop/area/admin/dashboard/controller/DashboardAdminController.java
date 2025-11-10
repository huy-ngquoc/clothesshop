package vn.uit.clothesshop.area.admin.dashboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardAdminController {
    @GetMapping("/admin")
    public String getDashboard() {
        return "admin/dashboard/show";
    }

}
