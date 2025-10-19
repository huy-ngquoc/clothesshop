package vn.uit.clothesshop.datarefactor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import vn.uit.clothesshop.domain.entity.Product;
import vn.uit.clothesshop.domain.entity.ProductVariant;
import vn.uit.clothesshop.repository.ProductRepository;
import vn.uit.clothesshop.repository.ProductVariantRepository;

//@Component
public class ProductDataRefactor {
    private final ProductRepository productRepo;
    private final ProductVariantRepository productVariantRepo;
    public ProductDataRefactor(ProductRepository productRepo, ProductVariantRepository productVariantRepo) {
        this.productRepo = productRepo;
        this.productVariantRepo= productVariantRepo;

        List<Product> listProduct = this.productRepo.findAll();
        List<Long> listProductId = listProduct.stream().map(Product::getId).toList();
        listProduct = new ArrayList<>();
        for(int i=0;i<listProductId.size();i++) {
            
            List<ProductVariant> listVariants = this.productVariantRepo.findByProduct_Id(listProductId.get(i));
            if(listVariants!=null&&listVariants.size()>0) {
                ProductVariant firstVariant = listVariants.get(0);
                Product p= firstVariant.getProduct();
                p.setImage(firstVariant.getImage());
                int sold=0;
                int quantity=0;
                int maxPrice=0;
                int minPrice=Integer.MAX_VALUE;
                for(int j=0;j<listVariants.size();j++) {
                    sold=sold+listVariants.get(j).getSold();
                    quantity=quantity+listVariants.get(j).getStockQuantity();
                    maxPrice =Integer.max(maxPrice, listVariants.get(j).getPriceCents());
                    minPrice = Integer.min(minPrice, listVariants.get(j).getPriceCents());
                }
                p.setSold(sold);
                p.setQuantity(quantity);
                p.setMaxPrice(maxPrice);
                p.setMinPrice(minPrice);
                listProduct.add(p);
            }

        }
        productRepo.saveAll(listProduct);
    }
}
