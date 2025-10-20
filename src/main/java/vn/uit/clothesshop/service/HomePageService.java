package vn.uit.clothesshop.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import vn.uit.clothesshop.domain.entity.Category;
import vn.uit.clothesshop.domain.entity.Product;
import vn.uit.clothesshop.dto.selectcolumninteface.ColorCount;
import vn.uit.clothesshop.dto.selectcolumninteface.SizeCount;

@Service
public class HomePageService {
    private final CategoryService categoryService;
    private final ProductVariantService productVariantService;
    private final ProductService productService;
    public HomePageService(CategoryService categoryService, ProductVariantService productVariantService, ProductService productService) {
        this.productVariantService= productVariantService;
        this.categoryService= categoryService;
        this.productService = productService;
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
    public Page<Product> getProductsPage(int page, int number) {
        return productService.getProductByPage(page, number);
    }
}
