package vn.uit.clothesshop.specification;

import org.springframework.data.jpa.domain.Specification;

import vn.uit.clothesshop.domain.entity.Product;
import vn.uit.clothesshop.domain.entity.Product_;

public class ProductSpecification {
    public static Specification<Product> nameLike(String name){
        
        return (root, query, criteriaBuilder) 
            -> criteriaBuilder.like(root.get(Product_.NAME), "%"+name+"%");
    }
}
