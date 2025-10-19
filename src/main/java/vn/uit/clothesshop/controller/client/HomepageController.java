package vn.uit.clothesshop.controller.client;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vn.uit.clothesshop.domain.entity.Product;
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

    @GetMapping("/shop")
    public String getShopPage(final Model model, @RequestParam(defaultValue="1") int page) {
        List<Product> listProducts = homePageService.getProducts(12, page);
        model.addAttribute("products",listProducts);
        return "client/homepage/shop";
    }
}
