package vn.uit.clothesshop.specification;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import vn.uit.clothesshop.domain.entity.Product;
import vn.uit.clothesshop.domain.entity.Product_;

public class ProductSpecification {
    public static Specification<Product> nameLike(String name){
        
        return (root, query, criteriaBuilder) 
            -> criteriaBuilder.like(root.get(Product_.NAME), "%"+name+"%");
    }
    public static Specification<Product> priceBetween(int from, int to) {
        Specification<Product> biggerThanFrom = (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(Product_.MIN_PRICE),from);
        Specification<Product> smallerThanTo = (root,query, criteriaBuilder)->criteriaBuilder.lessThanOrEqualTo(root.get(Product_.MIN_PRICE),to);
        return biggerThanFrom.and(smallerThanTo);
    }

    public static Specification<Product> idIn(Set<Long> listIds) {
    
        Specification<Product> idInSpec = (root, query,criteriaBuilder)->{
            CriteriaBuilder.In<Long> inClause = criteriaBuilder.in(root.get(Product_.id));
                for (Long id : listIds) {
                    inClause.value(id);
                }
                return inClause;
        };
        return idInSpec;
    }
    public static Specification<Product> andTwoSpec(Specification<Product> spec1, Specification<Product> spec2) {
        if(spec1==null) return spec2;
        return spec1.and(spec2);
    }
}
