package vn.uit.clothesshop.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import vn.uit.clothesshop.domain.Product;
import vn.uit.clothesshop.domain.ProductVariant;
import vn.uit.clothesshop.repository.ProductVariantRepository;

@Service
@Slf4j
public class ProductVariantService {
    @NotNull
    private ProductVariantRepository productVariantRepository;

    @NotNull
    private ProductService productService;

    public ProductVariantService(
            @NotNull final ProductVariantRepository productVariantRepository,
            @NotNull final ProductService productService) {
        this.productVariantRepository = productVariantRepository;
        this.productService = productService;
    }

    @Nullable
    public ProductVariant findProductVariantById(final long id) {
        return this.productVariantRepository.findById(id).orElse(null);
    }

    @NotNull
    public List<ProductVariant> findAllProductVariantsByProductId(final long productId) {
        final var product = this.productService.findProductById(productId);
        if (product == null) {
            return new ArrayList<>(0);
        }

        return this.findAllProductVariantsByProduct(product);
    }

    @Nullable
    public List<ProductVariant> findAllProductVariantsByProduct(@NotNull final Product product) {
        return this.productVariantRepository.findAllByProduct(product);
    }

    @Nullable
    private ProductVariant handleSaveProductVariant(final ProductVariant productVariant) {
        try {
            return this.productVariantRepository.save(productVariant);
        } catch (final Exception exception) {
            log.error("Error saving product variant", exception);
            return null;
        }
    }
}
