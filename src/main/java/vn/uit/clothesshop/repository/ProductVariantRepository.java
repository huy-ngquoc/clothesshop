package vn.uit.clothesshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.validation.constraints.NotNull;
import vn.uit.clothesshop.domain.Product;
import vn.uit.clothesshop.domain.ProductVariant;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    @NotNull
    List<@NotNull ProductVariant> findAllByProduct(final Product product);

    @NotNull
    public List<ProductVariant> findByProduct_Id(long productId);

    public void deleteByProduct_Id(long productId);
}
