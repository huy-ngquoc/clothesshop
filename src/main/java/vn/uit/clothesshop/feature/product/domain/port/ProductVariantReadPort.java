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
import vn.uit.clothesshop.feature.product.domain.ProductVariant;
import vn.uit.clothesshop.feature.product.infra.jpa.projection.ProductPriceBound;
import vn.uit.clothesshop.feature.product.infra.jpa.projection.ProductVariantColorCount;
import vn.uit.clothesshop.feature.product.infra.jpa.projection.ProductVariantSizeCount;

@Transactional(readOnly = true)
public interface ProductVariantReadPort {
    default Page<ProductVariant> findAll(
            @NonNull final Pageable pageable) {
        return this.findAll(null, pageable);
    }

    @NonNull
    Page<ProductVariant> findAll(
            @Nullable final Specification<ProductVariant> spec,
            @NonNull final Pageable pageable);

    @NonNull
    List<ProductVariant> findAllById(@NonNull final Iterable<Long> ids);

    @NonNull
    default Map<Long, ProductVariant> findMapById(@NonNull Iterable<Long> ids) {
        final var variants = this.findAllById(ids);
        return variants.stream()
                .collect(Collectors.toMap(ProductVariant::getId, Function.identity()));
    }

    Optional<ProductVariant> findById(final long id);

    ProductPriceBound findPriceBoundByProductId(final long productId);

    List<ProductVariantColorCount> countGroupedByColor();

    List<ProductVariantSizeCount> countGroupedBySize();

    List<Long> getProductIdByColor(final List<String> listColor);

    List<Long> getProductIdBySize(final List<String> listSize);

    Optional<Long> findProductIdById(final long id);
}
