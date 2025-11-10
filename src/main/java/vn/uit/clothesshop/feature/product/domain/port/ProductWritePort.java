package vn.uit.clothesshop.feature.product.domain.port;

import org.springframework.lang.NonNull;

import vn.uit.clothesshop.feature.product.domain.Product;

public interface ProductWritePort {
    Product save(@NonNull Product product);

    void deleteById(long id);

    void delete(@NonNull Product product);

    void updatePriceBoundFromVariants(long id);
}
