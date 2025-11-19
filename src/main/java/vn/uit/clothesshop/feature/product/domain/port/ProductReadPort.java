package vn.uit.clothesshop.feature.product.domain.port;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Nullable;
import vn.uit.clothesshop.feature.product.domain.Product;

@Transactional(readOnly = true)
public interface ProductReadPort {
    default Page<Product> findAll(
            @NonNull final Pageable pageable) {
        return this.findAll(null, pageable);
    }

    @NonNull
    Page<Product> findAll(
            @Nullable final Specification<Product> spec,
            @NonNull final Pageable pageable);

    @NonNull
    List<Product> findAllById(@NonNull final Iterable<Long> ids);

    Optional<Product> findById(final long id);

    Product getReferenceById(final long id);

    boolean existsById(final long id);

    @NonNull
    default Map<Long, Product> findMapById(@NonNull Iterable<Long> ids) {
        final var products = this.findAllById(ids);
        return products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
    }
}
