package vn.uit.clothesshop.area.site.recommendation.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import vn.uit.clothesshop.feature.product.domain.Product;
import vn.uit.clothesshop.feature.product.domain.enums.ETarget;
import vn.uit.clothesshop.feature.product.infra.jpa.repository.ProductRepository;
import vn.uit.clothesshop.feature.recommendation.domain.Model;
import vn.uit.clothesshop.feature.recommendation.util.ApiCall;
import vn.uit.clothesshop.feature.recommendation.util.RecommendationUtil;

@Service
public class RecommendationService {
    private final ProductRepository productRepo;

    public RecommendationService(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    public List<Product> recommendProduct(MultipartFile image) throws IOException {
        Model response = ApiCall.callApi(image);

        List<ETarget> listTargets = RecommendationUtil.listTargetFromRecommendationModel(response);
        return productRepo.findByTargets_In(listTargets);
    }
}
