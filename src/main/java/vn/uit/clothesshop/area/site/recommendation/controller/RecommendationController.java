package vn.uit.clothesshop.area.site.recommendation.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import vn.uit.clothesshop.area.site.recommendation.service.RecommendationService;
import vn.uit.clothesshop.feature.recommendation.domain.CompositeResponse;

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
    public String recommendationProduct(@RequestParam("outfitImage") MultipartFile image, Model model)
            throws IOException {
        CompositeResponse response = recommendationService.recommendProduct(image);
        
        model.addAttribute("recommendproducts", response.getProducts());
        model.addAttribute("result", response.getModel());
        return "client/recommendation/recommendationresult";
    }
}
