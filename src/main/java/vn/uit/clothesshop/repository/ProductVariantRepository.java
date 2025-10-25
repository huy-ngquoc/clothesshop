package vn.uit.clothesshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.validation.constraints.NotNull;
import vn.uit.clothesshop.domain.entity.Product;
import vn.uit.clothesshop.domain.entity.ProductVariant;
import vn.uit.clothesshop.repository.projection.ProductVariantColorCount;
import vn.uit.clothesshop.repository.projection.ProductInfoHomePage;
import vn.uit.clothesshop.repository.projection.ProductVariantSizeCount;

public interface ProductVariantRepository
        extends JpaRepository<ProductVariant, Long>, JpaSpecificationExecutor<ProductVariant> {
    @NotNull
    List<@NotNull ProductVariant> findAllByProduct(final Product product);

    @NotNull
    List<ProductVariant> findByProduct_Id(long productId);

    void deleteByProduct_Id(long productId);

    @Query("""
            SELECT pv.color AS color,
                COUNT(pv) AS total
            FROM ProductVariant pv
            GROUP BY pv.color
            """)
    @NotNull
    List<@NotNull ProductVariantColorCount> countByColor();

    @Query("""
            SELECT pv.size AS size,
                COUNT(pv) AS total
            FROM ProductVariant pv
            GROUP BY pv.size
            """)
    @NotNull
    List<@NotNull ProductVariantSizeCount> countBySize();

    @Query("""
            SELECT pv.image AS image,
                pv.product.name AS productName,
                pv.product.shortDesc AS productDescription
            FROM ProductVariant pv
            """)
    @NotNull
    List<@NotNull ProductInfoHomePage> getProductInfoForHomePage();

    @Query("""
            SELECT pv
            FROM ProductVariant pv
            WHERE pv.product.id IN :productIds
            """)
    @NotNull
    List<@NotNull ProductVariant> findByProductIdIn(@Param("productIds") List<Long> productIds);

    @Query("""
            SELECT pv.id
            FROM ProductVariant pv
            WHERE pv.color IN :listColor
            """)
    @NotNull
    List<@NotNull Long> findByColorIn(List<String> listColor);

    @Query("""
            SELECT pv.id
            FROM ProductVariant pv
            WHERE pv.size IN :listSize
            """)
    @NotNull
    List<@NotNull Long> findBySizeIn(List<String> listSize);
}
