package vn.uit.clothesshop.controller.client;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;

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
    public String getShopPage(final Model model, @RequestParam("page") Optional<String> pageOptional, @RequestParam("name") Optional<String> nameOptional) {
        String pageString = pageOptional.orElse("1");
        int pageNumber=1;
        String name = nameOptional.orElse(null);
        try {
            pageNumber = Integer.parseInt(pageString);
        }
        catch(Exception e) {
            return "client/homepage/show";
        }
        Page<Product> pageProducts= homePageService.getProductsPage(12, pageNumber,name);
        List<Product> listProducts = pageProducts.getContent();
        model.addAttribute("products",listProducts);
        model.addAttribute("currentPage",pageProducts.getNumber());
        model.addAttribute("totalPages",pageProducts.getTotalPages());
        return "client/homepage/shop";
    }
}
