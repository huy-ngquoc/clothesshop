package vn.uit.clothesshop.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vn.uit.clothesshop.product.domain.Product;
import vn.uit.clothesshop.product.domain.enumerator.ETarget;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    public List<Product> findByTargets_In(List<ETarget> listTargets);
}
