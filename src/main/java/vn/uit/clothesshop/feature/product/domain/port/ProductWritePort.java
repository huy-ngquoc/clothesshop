package vn.uit.clothesshop.feature.product.domain.port;

import java.util.List;

import org.springframework.lang.NonNull;

import vn.uit.clothesshop.feature.product.domain.Product;

public interface ProductWritePort {
    Product save(@NonNull Product product);

    List<Product> saveAll(@NonNull Iterable<Product> products);

    void deleteById(long id);

    void delete(@NonNull Product product);

    void updatePriceBoundFromVariants(long id);
}
