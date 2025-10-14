package vn.uit.clothesshop.controller.client;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import vn.uit.clothesshop.service.HomePageService;

@Controller
public class HomepageController {
    private final HomePageService homePageService;
    public HomepageController(HomePageService homePageService) {
        this.homePageService = homePageService;
    }
    @GetMapping("/")
    public String getHomepage(final Model model) {
        model.addAttribute("categories",homePageService.getAllCategories());
        model.addAttribute("sizeCounts", homePageService.getSizeCounts());
        model.addAttribute("colorCounts", homePageService.getColorCounts());
        return "client/homepage/show";
    }
}
