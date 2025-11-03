package vn.uit.clothesshop.product.domain;

import java.util.Optional;

public interface ProductAccess {
    Optional<Product> findById(final long id);

    boolean existsById(final long id);
}
