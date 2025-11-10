package vn.uit.clothesshop.feature.product.domain.port;

import org.springframework.lang.NonNull;

import vn.uit.clothesshop.feature.product.domain.ProductVariant;

public interface ProductVariantWritePort {
    @NonNull
    ProductVariant save(@NonNull final ProductVariant variant);

    void deleteById(final long id);

    void delete(@NonNull final ProductVariant variant);
}
