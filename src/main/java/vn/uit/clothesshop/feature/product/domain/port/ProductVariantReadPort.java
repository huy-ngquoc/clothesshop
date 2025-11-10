package vn.uit.clothesshop.feature.product.domain.port;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import jakarta.annotation.Nullable;
import vn.uit.clothesshop.feature.product.domain.ProductVariant;
import vn.uit.clothesshop.feature.product.infra.jpa.projection.ProductPriceBound;
import vn.uit.clothesshop.feature.product.infra.jpa.projection.ProductVariantColorCount;
import vn.uit.clothesshop.feature.product.infra.jpa.projection.ProductVariantSizeCount;

public interface ProductVariantReadPort {
    default Page<ProductVariant> findAll(
            @NonNull final Pageable pageable) {
        return this.findAll(null, pageable);
    }

    @NonNull
    Page<ProductVariant> findAll(
            @Nullable final Specification<ProductVariant> spec,
            @NonNull final Pageable pageable);

    Optional<ProductVariant> findById(final long id);

    ProductPriceBound findPriceBoundByProductId(final long productId);

    List<ProductVariantColorCount> countGroupedByColor();

    List<ProductVariantSizeCount> countGroupedBySize();

    List<Long> getProductIdByColor(final List<String> listColor);

    List<Long> getProductIdBySize(final List<String> listSize);

    Optional<Long> findProductIdById(final long id);
}
