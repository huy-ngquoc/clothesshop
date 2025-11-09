package vn.uit.clothesshop.recommendation.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import vn.uit.clothesshop.product.domain.Product;
import vn.uit.clothesshop.recommendation.service.RecommendationService;

@Controller
public class RecommendationController {
    private final RecommendationService recommendationService;
    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    } 
    @GetMapping("/recommendation")
    public String getRecommendationPage(final Model model) {
        return "client/recommendation/recommendationform";
    }

    @PostMapping("/recommendation")
    public String recommendationProduct(@RequestParam("outfitImage") MultipartFile image, Model model) throws IOException {
        List<Product> listRecommend =recommendationService.recommendProduct(image);
        System.out.println(listRecommend.size());
        model.addAttribute("recommendproducts", listRecommend);
        return "client/recommendation/recommendationresult";
    }
}
