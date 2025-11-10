package vn.uit.clothesshop.feature.product.infra.jpa.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import vn.uit.clothesshop.feature.product.domain.ProductVariant;
import vn.uit.clothesshop.feature.product.domain.port.ProductVariantReadPort;
import vn.uit.clothesshop.feature.product.domain.port.ProductVariantWritePort;
import vn.uit.clothesshop.feature.product.infra.jpa.projection.ProductPriceBound;
import vn.uit.clothesshop.feature.product.infra.jpa.projection.ProductVariantColorCount;
import vn.uit.clothesshop.feature.product.infra.jpa.projection.ProductVariantSizeCount;
import vn.uit.clothesshop.feature.product.infra.jpa.repository.ProductVariantRepository;

@Repository
class JpaProductVariantAdapter implements ProductVariantReadPort, ProductVariantWritePort {
    private final ProductVariantRepository repo;

    public JpaProductVariantAdapter(
            final ProductVariantRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public Page<ProductVariant> findAll(
            @Nullable Specification<ProductVariant> spec,
            @NonNull Pageable pageable) {
        return this.repo.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductVariant> findById(final long id) {
        return this.repo.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductPriceBound findPriceBoundByProductId(long productId) {
        return this.repo.findPriceBoundByProductId(productId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductVariantColorCount> countGroupedByColor() {
        return this.repo.countGroupedByColor();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductVariantSizeCount> countGroupedBySize() {
        return this.repo.countGroupedBySize();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> getProductIdByColor(List<String> listColor) {
        return this.repo.findByColorIn(listColor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> getProductIdBySize(List<String> listSize) {
        return this.repo.findBySizeIn(listSize);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Long> findProductIdById(final long id) {
        return this.repo.findProductIdById(id);
    }

    @Override
    @Transactional
    @NonNull
    public ProductVariant save(@NonNull ProductVariant variant) {
        return this.repo.save(variant);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        this.repo.deleteById(id);
    }

    @Override
    @Transactional
    public void delete(@NonNull ProductVariant variant) {
        this.repo.delete(variant);
    }
}
