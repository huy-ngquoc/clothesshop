package vn.uit.clothesshop.product.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.uit.clothesshop.product.domain.ProductVariant;
import vn.uit.clothesshop.product.domain.ProductVariantAccess;
import vn.uit.clothesshop.product.repository.ProductVariantRepository;
import vn.uit.clothesshop.product.repository.projection.ProductPriceBound;

@Service
@Transactional(readOnly = true)
class JpaProductVariantAccess implements ProductVariantAccess {
    private final ProductVariantRepository repository;

    public JpaProductVariantAccess(
            final ProductVariantRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<ProductVariant> findById(final long id) {
        return this.repository.findById(id);
    }

    @Override
    public ProductPriceBound findPriceBoundByProductId(long productId) {
        return this.repository.findPriceBoundByProductId(productId);
    }
}
