package vn.uit.clothesshop.feature.product.infra.jpa.adapter;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Nullable;
import vn.uit.clothesshop.feature.product.domain.Product;
import vn.uit.clothesshop.feature.product.domain.port.ProductReadPort;
import vn.uit.clothesshop.feature.product.domain.port.ProductWritePort;
import vn.uit.clothesshop.feature.product.infra.jpa.repository.ProductRepository;
import vn.uit.clothesshop.feature.product.infra.jpa.repository.ProductVariantRepository;

@Repository
class JpaProductAdapter implements ProductReadPort, ProductWritePort {
    private final ProductRepository productRepo;
    private final ProductVariantRepository variantRepo;

    public JpaProductAdapter(
            final ProductRepository productRepo,
            final ProductVariantRepository variantRepo) {
        this.productRepo = productRepo;
        this.variantRepo = variantRepo;
    }

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public Page<Product> findAll(
            @Nullable final Specification<Product> spec,
            @NonNull final Pageable pageable) {
        return this.productRepo.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findById(final long id) {
        return this.productRepo.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public Product getReferenceById(final long id) {
        return this.productRepo.getReferenceById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(final long id) {
        return this.productRepo.existsById(id);
    }

    @Override
    @Transactional
    public Product save(@NonNull final Product product) {
        return this.productRepo.save(product);
    }

    @Override
    @Transactional
    public void deleteById(final long id) {
        this.productRepo.deleteById(id);
    }

    @Override
    @Transactional
    public void delete(@NonNull final Product product) {
        this.productRepo.delete(product);
    }

    @Override
    @Transactional
    public void updatePriceBoundFromVariants(final long id) {
        final var priceBound = variantRepo.findPriceBoundByProductId(id);
        if (priceBound == null) {
            return;
        }

        final var product = productRepo.getReferenceById(id);
        product.setMinPrice(priceBound.getMin());
        product.setMaxPrice(priceBound.getMax());
    }

}
