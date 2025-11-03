package vn.uit.clothesshop.product.domain;

import java.util.Optional;

import vn.uit.clothesshop.product.repository.projection.ProductPriceBound;

public interface ProductVariantAccess {
    Optional<ProductVariant> findById(final long id);

    ProductPriceBound findPriceBoundByProductId(final long productId);
}
