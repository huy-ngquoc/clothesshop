package vn.uit.clothesshop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.uit.clothesshop.domain.entity.Category;
import vn.uit.clothesshop.dto.selectcolumninteface.ColorCount;
import vn.uit.clothesshop.dto.selectcolumninteface.SizeCount;

@Service
public class HomePageService {
    private final CategoryService categoryService;
    private final ProductVariantService productVariantService;
    public HomePageService(CategoryService categoryService, ProductVariantService productVariantService) {
        this.productVariantService= productVariantService;
        this.categoryService= categoryService;
    }
    public List<Category> getAllCategories() {
        return categoryService.findAll();
    }
    public List<SizeCount> getSizeCounts() {
        return productVariantService.countProductVariantBySize();
    } 
    public List<ColorCount> getColorCounts() {
        return productVariantService.countProductVariantByColor();
    }
}
