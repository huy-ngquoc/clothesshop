package vn.uit.clothesshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import jakarta.validation.constraints.NotNull;
import vn.uit.clothesshop.domain.Product;
import vn.uit.clothesshop.domain.ProductVariant;
import vn.uit.clothesshop.dto.selectcolumninteface.ColorCount;
import vn.uit.clothesshop.dto.selectcolumninteface.ProductInfoHomePage;
import vn.uit.clothesshop.dto.selectcolumninteface.SizeCount;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    @NotNull
    List<@NotNull ProductVariant> findAllByProduct(final Product product);

    @NotNull
    public List<ProductVariant> findByProduct_Id(long productId);

    public void deleteByProduct_Id(long productId);

    @Query("SELECT pv.color AS color, COUNT(pv) as count from ProductVariant pv GROUP BY pv.color")
    public List<ColorCount> countByColor();

    @Query("SELECT pv.size AS size, COUNT(pv) as count from ProductVariant pv GROUP BY pv.size")
    public List<SizeCount> countBySize();

    @Query("SELECT pv.image as image, pv.product.name as productName, pv.product.shortDesc as productDescription from ProductVariant pv")
    public List<ProductInfoHomePage> getProductInfoForHomePage();

    public List<ProductVariant> findByProduct_IdIn(List<Long> listProductId);
}
