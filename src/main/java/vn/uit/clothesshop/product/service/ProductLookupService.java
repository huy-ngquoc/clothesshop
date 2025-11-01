package vn.uit.clothesshop.product.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import vn.uit.clothesshop.product.domain.Product;
import vn.uit.clothesshop.product.repository.ProductRepository;

@Service
class ProductLookupService {
    @NotNull
    private final ProductRepository productRepository;

    ProductLookupService(
            @NotNull final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    @NotNull
    List<@NotNull Product> findAll() {
        return this.productRepository.findAll();
    }

    @Transactional(readOnly = true)
    @NotNull
    public Page<@NotNull Product> findAll(
            @Nullable final Specification<Product> spec,
            @NotNull final Pageable pageable) {
        return this.productRepository.findAll(spec, pageable);
    }

    @Transactional(readOnly = true)
    @Nullable
    Product findById(final long id) {
        return this.productRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    boolean existsById(final long id) {
        return this.productRepository.existsById(id);
    }
}
