package vn.uit.clothesshop.util;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import vn.uit.clothesshop.feature.product.domain.Product;
import vn.uit.clothesshop.feature.product.domain.ProductVariant;
import vn.uit.clothesshop.feature.product.infra.jpa.repository.ProductRepository;
import vn.uit.clothesshop.feature.product.infra.jpa.repository.ProductVariantRepository;

//@Component
public class ProductRefactor {
    private final ProductRepository productRepo;
    private final ProductVariantRepository productVariantRepo;
    public ProductRefactor(ProductRepository productRepo, ProductVariantRepository productVariantRepo) {
        this.productRepo = productRepo;
        this.productVariantRepo = productVariantRepo;
        List<Product> listProducts = productRepo.findAll();
        for(Product p: listProducts) {
            List<ProductVariant> productVariants = productVariantRepo.findByProduct_Id(p.getId());
            int productAmount = 0;
            for(ProductVariant pv:productVariants) {
                productAmount+=pv.getStockQuantity();
            }
            p.setQuantity(productAmount);

        }
        productRepo.saveAll(listProducts);
    } 

    
}
