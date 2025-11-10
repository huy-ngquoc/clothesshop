package vn.uit.clothesshop.feature.product.domain.port;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import jakarta.annotation.Nullable;
import vn.uit.clothesshop.feature.product.domain.Product;

public interface ProductReadPort {
    default Page<Product> findAll(
            @NonNull final Pageable pageable) {
        return this.findAll(null, pageable);
    }

    @NonNull
    Page<Product> findAll(
            @Nullable final Specification<Product> spec,
            @NonNull final Pageable pageable);

    Optional<Product> findById(final long id);

    Product getReferenceById(final long id);

    boolean existsById(final long id);
}
