package vn.uit.clothesshop.product.domain;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import jakarta.annotation.Nullable;
import vn.uit.clothesshop.product.repository.projection.ProductPriceBound;

public interface ProductVariantAccess {
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
}
